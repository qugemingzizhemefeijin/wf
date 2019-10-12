package cg.zz.wf.mvc.actionresult;

import org.apache.commons.codec.Charsets;

import cg.zz.wf.core.json.JacksonSupportJson;
import cg.zz.wf.core.json.Json;
import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 支持返回JSON的ActionResult
 * 
 * @author chengang
 *
 */
public class JsonActionResult extends ActionResult {
	
	private static final Json JSON = JacksonSupportJson.buildNormalBinder();
	
	private Object result = null;
	private String encoding = Charsets.UTF_8.name();
	
	public JsonActionResult(Object result, String encoding) {
		this.result = result;
		this.encoding = encoding;
	}

	public JsonActionResult(Object result) {
		this.result = result;
	}

	@Override
	public void render(BeatContext beat) throws Exception {
		if (this.encoding == null) {
			this.encoding = Charsets.UTF_8.name();
		}
		
		beat.getResponse().setCharacterEncoding(this.encoding);
		beat.getResponse().setContentType("text/json");
		beat.getResponse().getWriter().print(this.result == null ? "{}" : JSON.toJson(this.result));
		beat.getResponse().getWriter().close();
	}

}
