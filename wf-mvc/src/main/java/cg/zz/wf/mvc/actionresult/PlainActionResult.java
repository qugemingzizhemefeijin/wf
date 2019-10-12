package cg.zz.wf.mvc.actionresult;

import org.apache.commons.codec.Charsets;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 普通文本的ActionResult
 * 
 * @author chengang
 *
 */
public class PlainActionResult extends ActionResult {

	private String text = null;
	private String encoding = Charsets.UTF_8.name();

	public PlainActionResult(String text, String encoding) {
		this.text = text;
		this.encoding = encoding;
	}

	public PlainActionResult(String text) {
		this.text = text;
	}

	@Override
	public void render(BeatContext beat) throws Exception {
		if (this.encoding == null) {
			this.encoding = Charsets.UTF_8.name();
		}
		beat.getResponse().setCharacterEncoding(this.encoding);
		if (this.text == null) {
			this.text = "";
		}
		beat.getResponse().setContentType("text/plain");
		beat.getResponse().getWriter().print(this.text);
		beat.getResponse().getWriter().close();
	}

}
