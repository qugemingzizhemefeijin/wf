package cg.zz.wf.mvc.server;

import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.client.CookieHandler;

/**
 * 
 * 服务器端环境
 * 
 * @author chengang
 *
 */
public class ServerContext {

	BeatContext beat;
	CookieHandler cookie;
	SessionHandler session;

	public ServerContext(BeatContext beat) {
		this.beat = beat;
	}

	public SessionHandler getSessions() {
		if (this.session == null) {
			this.session = new BaseSessionHandler(this.beat);
		}
		return this.session;
	}

	public String getRealPath() {
		return this.beat.getServletContext().getRealPath("/");
	}

	public String getContextPath() {
		return this.beat.getServletContext().getContextPath();
	}

}
