package cg.zz.wf.mvc.route.interceptor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.annotation.Interceptor;
import cg.zz.wf.mvc.annotation.Interceptor.InterceptorType;
import cg.zz.wf.mvc.interceptor.ActionInterceptor;
import cg.zz.wf.mvc.route.ActionInfo;

/**
 * 
 * 拦截器处理类
 * 
 * @author chengang
 *
 */
public class InterceptorHandler {

	/**
	 * Class是ActionInterceptor实现类，ActionInterceptor是其实例化对象
	 */
	private static Map<Class<?>, ActionInterceptor> actionInterceptorMap = new HashMap<>();

	/**
	 * 请求拦截器集合
	 */
	List<OrderActionInterceptor> actionInterceptors = new ArrayList<>();
	
	
	/**
	 * 返回拦截器集合
	 */
	List<OrderActionInterceptor> resultInterceptors = new ArrayList<>();
	
	/**
	 * 异常拦截器集合
	 */
	List<OrderActionInterceptor> exceptionInterceptors = new ArrayList<>();

	/**
	 * 请求处理之前执行所有的拦截逻辑
	 * @param beat - BeatContext
	 * @return ActionResult
	 */
	public ActionResult preExecute(BeatContext beat) {
		for (OrderActionInterceptor oai : this.actionInterceptors) {
			ActionResult result = oai.interceptor.preExecute(beat);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * 请求处理完成后执行所有的拦截逻辑
	 * @param beat - BeatContext
	 * @return ActionResult
	 */
	public ActionResult afterExecute(BeatContext beat) {
		for (OrderActionInterceptor oai : this.resultInterceptors) {
			ActionResult result = oai.interceptor.preExecute(beat);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * 当请求处理发生异常的时候执行所有的拦截逻辑
	 * @param beat - BeatContext
	 * @return ActionResult
	 */
	public ActionResult exceptionExecute(BeatContext beat) {
		for (OrderActionInterceptor oai : this.exceptionInterceptors) {
			ActionResult result = oai.interceptor.preExecute(beat);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * 将拦截器添加到集合中维护
	 * @param ai - ActionInterceptor
	 * @param order - 拦截器的顺序
	 * @param type - 拦截器类型
	 */
	public void add(ActionInterceptor ai, int order, Interceptor.InterceptorType type) {
		//添加请求前拦截器
		if (type == Interceptor.InterceptorType.ACTION) {
			int index = 0;
			//计算拦截器应该放置的位置
            while (index < this.actionInterceptors.size() && order >= this.actionInterceptors.get(index).order) {
                index++;
            }
			this.actionInterceptors.add(index, new OrderActionInterceptor(order, ai));
		}
		//添加请求后拦截器
		if (type == Interceptor.InterceptorType.RESULT) {
			int index2 = 0;
			//计算拦截器应该放置的位置
            while (index2 < this.resultInterceptors.size() && order >= this.resultInterceptors.get(index2).order) {
                index2++;
            }
            this.resultInterceptors.add(index2, new OrderActionInterceptor(order, ai));
		}
		//添加异常拦截器
		if (type == Interceptor.InterceptorType.EXECEPTION) {
			int index3 = 0;
			//计算拦截器应该放置的位置
            while (index3 < this.exceptionInterceptors.size() && order >= this.exceptionInterceptors.get(index3).order) {
                index3++;
            }
            this.exceptionInterceptors.add(index3, new OrderActionInterceptor(order, ai));
		}
	}

	/**
	 * 针对每个ActionInfo，组装其支持的拦截器
	 * @param actionInfo - ActionInfo
	 */
	public void build(ActionInfo actionInfo) {
        int order;
        //获取ActionInfo中所有的注解，此注解包含方法和所属类上的所有的注解
        for (Annotation ann : actionInfo.getAnnotations()) {
        	//查看此注解是否有Interceptor注解
            Interceptor icp = AnnotationUtils.findAnnotation(ann.getClass(), Interceptor.class);
            if (icp != null) {
            	//获取注解的类型，到底是异常还是请求之前还是请求之后的拦截器
                InterceptorType type = icp.type();
                //创建拦截器类的实例
                ActionInterceptor interceptor = ActionInterceptorFactory(icp.value());
                //获取其order值
                Object orderObject = AnnotationUtils.getValue(ann, "order");
                if (orderObject == null) {
                    order = 1;
                } else {
                    order = ((Integer) orderObject).intValue();
                }
                //添加到对应的集合中
                add(interceptor, order, type);
            }
        }
    }

	/**
	 * 获取或创建拦截器执行逻辑的对象实例
	 * @param clazz - Class
	 * @return ActionInterceptor
	 */
	private static <T extends ActionInterceptor> ActionInterceptor ActionInterceptorFactory(Class<T> clazz) {
		ActionInterceptor actionInterceptor = actionInterceptorMap.get(clazz);
		if (actionInterceptor == null) {
			actionInterceptor = BeanUtils.instantiate(clazz);
			actionInterceptorMap.put(clazz, actionInterceptor);
		}
		return actionInterceptor;
	}

}
