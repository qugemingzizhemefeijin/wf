package cg.zz.wf.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 这个应该是拦截器的组合注解，支持多拦截的注解
 * 
 * @author chengang
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionInterceptors {
	
	Interceptor[] value();

}
