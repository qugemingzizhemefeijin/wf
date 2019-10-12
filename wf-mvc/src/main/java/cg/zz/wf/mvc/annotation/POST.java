package cg.zz.wf.mvc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * Post请求
 * 
 * @author chengang
 *
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface POST {

}
