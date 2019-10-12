package cg.zz.wf.mvc.route;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.annotation.Ignored;

/**
 * 
 * 辅助类
 * 
 * @author chengang
 *
 */
public final class RouteUtils {
	
	/**
	 * ReflectionUtils.doWithMethods方法判断是否能够处理的方法过滤器
	 */
	public static MethodFilter HANDLER_METHODS = new MethodFilter() {
        public boolean matches(Method method) {
            if (AnnotationUtils.findAnnotation(method, Ignored.class) != null) {
                return false;
            }
            Class<?> returnType = method.getReturnType();
            if (returnType == null || !ActionResult.class.isAssignableFrom(returnType) || method.isBridge() || method.getDeclaringClass() == Object.class || !Modifier.isPublic(method.getModifiers())) {
                return false;
            }
            return true;
        }
    };
	
	private RouteUtils() {
		
	}

}
