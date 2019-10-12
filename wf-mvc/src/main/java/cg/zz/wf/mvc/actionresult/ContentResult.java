package cg.zz.wf.mvc.actionresult;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;

public class ContentResult extends ActionResult {

	public String content = "";

	public ContentResult(String content) {
		this.content = content;
	}

	@Override
	public void render(BeatContext beat) throws Exception {
		beat.getResponse().getOutputStream().print(this.content);
	}

}
