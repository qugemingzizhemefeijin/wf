package cg.zz.wf.mvc.invoke;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import cg.zz.wf.core.log.Log;
import cg.zz.wf.core.log.LogFactory;
import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.bind.BindAndValidate;
import cg.zz.wf.mvc.bind.BindUtils;
import cg.zz.wf.mvc.bind.ObjectBindResult;
import cg.zz.wf.mvc.invoke.converter.ConverterFactory;
import cg.zz.wf.mvc.route.ActionInfo;
import cg.zz.wf.mvc.route.RouteResult;

/**
 * 
 * 请求方法的执行者
 * 
 * @author chengang
 *
 */
public class ActionInvoker {

	private static final Log LOGGER = LogFactory.getLog(ActionInvoker.class);
	
	private static ConverterFactory converter = new ConverterFactory();

	/**
	 * 反射调用具体的方法
	 * @param match - RouteResult
	 * @return Object
	 * @throws Exception
	 */
	public Object invoke(RouteResult match) throws Exception {
		try {
			ActionInfo action = match.action;
			Map<String, String> urlParams = match.urlParams;
			BeatContext beat = match.beat;
			
			//执行请求前置拦截器
			ActionResult preResult = action.getInterceptorHandler().preExecute(beat);
			if (preResult != null) {
				return preResult;
			}
			//获取方法需要的参数类型和参数名称
			Class<?>[] paramTypes = action.getParamTypes();
			String[] paramNames = action.getParamNames();
			Object[] param = new Object[paramTypes.length];
			for (int i = 0; i < paramNames.length; i++) {
				String paramName = paramNames[i];
				Class<?> clazz = paramTypes[i];
				String v = urlParams.get(paramName);
				//先查看是否是基本类型，如果是的话直接转换
				if (v != null && converter.canConvert(clazz)) {
					param[i] = converter.convert(clazz, v);
				} else if (!converter.canConvert(clazz)) {
					//如果不是基本类型，则需要通过Spring来执行对象绑定
					ObjectBindResult br = BindUtils.bind(clazz, beat);
					beat.getBindResults().add(br);
					param[i] = br.getTarget();
					//进行对象合法性校验
					beat.getBindResults().add(BindAndValidate.Singleton().validate(param[i]));
				}
			}
			long t1 = System.currentTimeMillis();
			Object result = null;
			try {
				//反射调用目标方法
				result = action.getActionMethod().invoke(action.getController(), param);
			} catch (Exception e) {
				LOGGER.error("Action Invoke", e);
				//如果发生了一场，则直接异常拦截器
				ActionResult exceptionResult = action.getInterceptorHandler().exceptionExecute(beat);
				if (exceptionResult != null) {
					return exceptionResult;
				}
			}
			long t = System.currentTimeMillis() - t1;
			//如果调用时间超过了100ms，则打印警告信息
			if (t >= 100) {
				LOGGER.info("time:" + t + "ms, url:" + beat.getClient().getRelativeUrl());
			}
			//执行请求后置拦截器
			ActionResult afterResult = action.getInterceptorHandler().afterExecute(beat);
			if (afterResult != null) {
				return afterResult;
			}
			return result;
		} catch (InvocationTargetException e2) {
			Throwable t2 = e2.getCause();
			if (t2 == null || !(t2 instanceof Exception)) {
				throw e2;
			}
			throw ((Exception) t2);
		}
	}

}
