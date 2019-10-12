package cg.zz.wf.mvc.route;

import java.util.HashMap;
import java.util.Map;

import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 通过BeatRouter路由匹配到要执行的方法结果对象
 * 
 * @author chengang
 *
 */
public class RouteResult {

	/**
	 * 维护的具体的请求对应的方法
	 */
	public ActionInfo action;
	
	/**
	 * 
	 */
	public String pattern;
	
	/**
	 * 解析出URL中的动态参数映射
	 */
	public Map<String, String> urlParams = new HashMap<>();
	
	/**
	 * BeatContext
	 */
	public BeatContext beat;

	public static interface PreProcessor {
		
		public void PreProcess(RouteResult paramRouteResult);

		public void Register(ActionInfo paramActionInfo);
	}

}
