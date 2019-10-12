package cg.zz.wf.mvc.cache;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.annotation.AnnotationUtils;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.annotation.OutputCache;
import cg.zz.wf.mvc.interceptor.ActionInterceptor;

/**
 * 
 * 缓存请求的拦截器
 * 
 * @author chengang
 *
 */
public class OutputCacheInterceptor implements ActionInterceptor {

	private Map<Method, Integer> methodMap = new ConcurrentHashMap<>();

	@Override
	public ActionResult preExecute(BeatContext beat) {
		int duration = -1;
		Method method = beat.getAction().getActionMethod();

		//缓存Method和时间的映射关系，不需要每次都动态的去获取方法的注解
		Integer intDuration = this.methodMap.get(method);

		duration = intDuration == null ? -1 : intDuration.intValue();
		if (duration == -1) {
			OutputCache oc = AnnotationUtils.findAnnotation(method, OutputCache.class);
			duration = oc.duration();
			if (duration < 0) {
				duration = 60;
			}
			this.methodMap.put(method, duration);
		}
		CacheContext cache = beat.getCache();

		//从缓存中获取ActionResult对象，如果为空，则初始化
		ActionResult result = cache.getCacheResult();
		if (result == null) {
			//设置缓存过期时间
			cache.setExpiredTime(duration);
			//初始化缓存相关的信息
			cache.needCache();
		}
		return result;
	}

}
