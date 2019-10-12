package cg.zz.wf.mvc.interceptor;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 拦截器接口
 * 
 * @author chengang
 *
 */
public interface ActionInterceptor {

	/**
	 * 拦截并处理的方法
	 * @param beat - BeatContext
	 * @return ActionResult
	 */
	public ActionResult preExecute(BeatContext beat);

}
