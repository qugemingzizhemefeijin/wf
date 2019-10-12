package cg.zz.wf.mvc;

import cg.zz.wf.mvc.view.RedirectResult;
import cg.zz.wf.mvc.view.VelocityViewResult;

/**
 * 
 * View层的显示对象
 * 
 * @author chengang
 *
 */
public abstract class ActionResult {

	/**
	 * 支持Velocity的ActionResult
	 * @param viewName - 模版名称
	 * @return ActionResult
	 */
	public static ActionResult view(String viewName) {
		//这段代码很申请啊，看来其实是只想走Velocity。
		//if ("vm".equals("vm")) {
			return new VelocityViewResult(viewName);
		//}
		//return new JspViewResult(viewName);
	}

	/**
	 * 支持跳转的ActionResult
	 * @param redirectUrl - 跳转地址
	 * @return ActionResult
	 */
	public static ActionResult redirect(String redirectUrl) {
		return new RedirectResult(redirectUrl);
	}

	/**
	 * 支持相对地址的跳转ActionResult<br/>
	 * 如：当前地址是 /index，传入/user，则跳转地址变成/index/user
	 * @param redirectRelativeUrl - String
	 * @return ActionResult
	 */
	public static ActionResult redirectContext(String redirectRelativeUrl) {
		return new ActionResult() {
			public void render(BeatContext beat) throws Exception {
				ActionResult result = redirect(beat.getServer().getContextPath() + redirectRelativeUrl);
				result.render(beat);
			}
		};
	}

	public static ActionResult Stream(String fileName) {
		return null;
	}

	public abstract void render(BeatContext beat) throws Exception;

}
