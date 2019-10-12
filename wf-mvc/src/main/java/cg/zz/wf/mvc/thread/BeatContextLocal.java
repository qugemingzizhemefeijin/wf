package cg.zz.wf.mvc.thread;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cg.zz.wf.mvc.ActionAttribute;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.Model;
import cg.zz.wf.mvc.bind.BeatBindResults;
import cg.zz.wf.mvc.cache.CacheContext;
import cg.zz.wf.mvc.client.ClientContext;
import cg.zz.wf.mvc.server.ServerContext;
import cg.zz.wf.mvc.trace.TraceInfo;

/**
 * 
 * 感觉没必须专门设计出此类
 * 
 * @author chengang
 *
 */
public class BeatContextLocal implements BeatContext {

	private BeatContext getCurrent() {
		return BeatContextUtils.getCurrent();
	}

	public HttpServletRequest getRequest() {
		return getCurrent().getRequest();
	}

	public HttpServletResponse getResponse() {
		return getCurrent().getResponse();
	}

	public Model getModel() {
		return getCurrent().getModel();
	}

	@SuppressWarnings("deprecation")
	public String getRelativeUrl() {
		return getCurrent().getRelativeUrl();
	}

	public BeatBindResults getBindResults() {
		return getCurrent().getBindResults();
	}

	public ClientContext getClient() {
		return getCurrent().getClient();
	}

	public ServletContext getServletContext() {
		return getCurrent().getServletContext();
	}

	public ServerContext getServer() {
		return getCurrent().getServer();
	}

	public ActionAttribute getAction() {
		return getCurrent().getAction();
	}

	public CacheContext getCache() {
		return getCurrent().getCache();
	}

	public TraceInfo getTraceInfo() {
		return getCurrent().getTraceInfo();
	}

	public void setParamWithoutValidate(String[] params) {
		getCurrent().setParamWithoutValidate(params);
	}

	public String[] getParamWithoutValidate() {
		return getCurrent().getParamWithoutValidate();
	}

}
