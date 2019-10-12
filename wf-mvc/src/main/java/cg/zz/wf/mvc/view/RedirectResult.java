package cg.zz.wf.mvc.view;

import java.io.IOException;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 支持页面跳转的ActionResult
 * 
 * @author chengang
 *
 */
public class RedirectResult extends ActionResult {

	/**
	 * 跳转的地址
	 */
	private String url;

	public RedirectResult(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	@Override
	public void render(BeatContext beat) throws IOException {
		beat.getResponse().sendRedirect(this.url);
	}

}
