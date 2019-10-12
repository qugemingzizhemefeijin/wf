package cg.zz.wf.mvc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cg.zz.wf.mvc.interceptor.ActionInterceptor;

/**
 * 
 * 拦截器注解
 * 
 * @author chengang
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD, PACKAGE })
@Documented
public @interface Interceptor {

	/**
	 * 值为请求拦截器具体的实现类
	 * @return Class<? extends ActionInterceptor>
	 */
	Class<? extends ActionInterceptor> value();

	/**
	 * 拦截器类型，默认是请求拦截器
	 * @return InterceptorType
	 */
	InterceptorType type() default InterceptorType.ACTION;

	/**
	 * 
	 * 拦截器类型枚举
	 * 
	 * @author chengang
	 *
	 */
	public static enum InterceptorType {
		
		/**
		 * 请求拦截器
		 */
		ACTION,
		
		/**
		 * 异常拦截器
		 */
		EXECEPTION,
		
		/**
		 * 返回拦截器
		 */
		RESULT;
	}

}
