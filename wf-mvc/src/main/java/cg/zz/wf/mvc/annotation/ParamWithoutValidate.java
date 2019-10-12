package cg.zz.wf.mvc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cg.zz.wf.mvc.interceptor.ParamValidateInterceptor;

@Interceptor(ParamValidateInterceptor.class)
@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface ParamWithoutValidate {
	
	String[] value() default {};

}
