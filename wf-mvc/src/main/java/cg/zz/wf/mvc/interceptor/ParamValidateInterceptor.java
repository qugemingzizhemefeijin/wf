package cg.zz.wf.mvc.interceptor;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.annotation.ParamWithoutValidate;

/**
 * 
 * 对方法的请求参数是否需设置哪些不需要值转换<br/>
 * 具体参考ParamWithoutValidate注解，其中value的值是一个数组，可以设置哪些请求参数不需要通过Converter.convert进行sql过滤和html过滤等
 * 
 * @author chengang
 *
 */
public class ParamValidateInterceptor implements ActionInterceptor {

	@Override
	public ActionResult preExecute(BeatContext beat) {
		Method method = beat.getAction().getActionMethod();

		ParamWithoutValidate nci = AnnotationUtils.findAnnotation(method, ParamWithoutValidate.class);
		if (nci != null) {
			String[] paramsWithoutValidate = nci.value();
			if(paramsWithoutValidate.length != 0) {
				beat.setParamWithoutValidate(paramsWithoutValidate);
			}
		}
		return null;
	}

}
