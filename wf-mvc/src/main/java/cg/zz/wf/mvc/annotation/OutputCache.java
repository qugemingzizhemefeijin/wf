package cg.zz.wf.mvc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cg.zz.wf.mvc.cache.OutputCacheInterceptor;

@Interceptor(OutputCacheInterceptor.class)
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface OutputCache {

	/**
	 * 拦截器顺序
	 * @return int
	 */
	int order() default 1;

	/**
	 * 缓存时间，单位秒
	 * @return int
	 */
	int duration() default 60;

}
