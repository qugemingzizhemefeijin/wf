package cg.zz.wf.mvc.actionresult;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import cg.zz.wf.mvc.ActionResult;
import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 文件下载的ActionResult
 * 
 * @author chengang
 *
 */
public class FileActionResult extends ActionResult {
	
	private static final String DEFAULT_CONTEXT_TYPE = "application/octet-stream";

	private byte[] buffer = null;
	private String filename = null;
	private InputStream fis = null;
	private String contextType = DEFAULT_CONTEXT_TYPE;

	public FileActionResult(byte[] buffer, String filename) {
		this.buffer = buffer;
		this.filename = filename;
	}

	public FileActionResult(byte[] buffer, String filename, String contextType) {
		this.buffer = buffer;
		this.filename = filename;
		this.contextType = contextType;
	}

	public FileActionResult(InputStream fis, String filename) {
		this.fis = fis;
		this.filename = filename;
	}

	public FileActionResult(InputStream fis, String filename, String contextType) {
		this.fis = fis;
		this.filename = filename;
		this.contextType = contextType;
	}

	@Override
	public void render(BeatContext beat) throws Exception {
		beat.getResponse().addHeader("Content-Disposition", "attachment;filename=" + new String(this.filename.getBytes()));
		if (this.fis != null) {
			byte[] nbuffer = new byte[this.fis.available()];
			this.fis.read(nbuffer);
			this.fis.close();
			OutputStream toClient = new BufferedOutputStream(beat.getResponse().getOutputStream());
			beat.getResponse().setContentType(this.contextType);
			toClient.write(nbuffer);
			toClient.flush();
			toClient.close();
		} else {
			OutputStream toClient = new BufferedOutputStream(beat.getResponse().getOutputStream());
			beat.getResponse().setContentType(this.contextType);
			toClient.write(this.buffer);
			toClient.flush();
			toClient.close();
		}
	}

}
