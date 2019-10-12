package cg.zz.wf.mvc.thread;

import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cg.zz.wf.mvc.ActionAttribute;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.Model;
import cg.zz.wf.mvc.Trace;
import cg.zz.wf.mvc.WFHttpServletRequestWrapper;
import cg.zz.wf.mvc.bind.BeatBindResults;
import cg.zz.wf.mvc.cache.CacheContext;
import cg.zz.wf.mvc.client.ClientContext;
import cg.zz.wf.mvc.client.UploadRequest;
import cg.zz.wf.mvc.server.ServerContext;
import cg.zz.wf.mvc.trace.TraceInfo;

/**
 * 
 * BeatContext的实现
 * 
 * @author chengang
 *
 */
public class BeatContextBean implements BeatContext {

	public static final String BEAT_MODEL_ATTRIBUTE = BeatContextBean.class.getName() + ".MODEL";
	public static ServletContext servletContext = null;
	private final Model model = new Model();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String relativeUrl = null;
	private String[] paramWithoutValidate = null;
	//private static UrlPathHelper urlPathHelper = new UrlPathHelper();
	ClientContext client;
	ServerContext server;
	CacheContext cache;
	private TraceInfo traceInfo;
	ActionAttribute action;
	private BeatBindResults bindResults = new BeatBindResults();

	public BeatContextBean(HttpServletRequest request, HttpServletResponse response) {
		//将当前的request包装成WFHttpServletRequestWrapper
		HttpServletRequest convertRequest = new WFHttpServletRequestWrapper(request, this);
		//判断是否是上传文件表单，如果是的话，再包装一层
		request = UploadRequest.wrapper(convertRequest);

		setRequest(request);

		setResponse(response);

		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		
		//设置请求的相对地址
		this.relativeUrl = uri.substring(contextPath.length());
		if (Objects.equals("/index.jsp", this.relativeUrl)) {
			this.relativeUrl = "/";
		}
		
		this.client = new ClientContext(this);
		this.server = new ServerContext(this);
		this.cache = new CacheContext(this);

		//初始化跟踪对象,请求必须带trace+当前时间才会执行跟踪逻辑
		Trace.init(this);
	}

	private void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public Model getModel() {
		return this.model;
	}

	public String getRelativeUrl() {
		return this.relativeUrl;
	}

	public BeatBindResults getBindResults() {
		return this.bindResults;
	}

	public ClientContext getClient() {
		return this.client;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public ServerContext getServer() {
		return this.server;
	}

	public ActionAttribute getAction() {
		return this.action;
	}

	public void setAction(ActionAttribute action) {
		this.action = action;
	}

	public CacheContext getCache() {
		return this.cache;
	}

	public TraceInfo getTraceInfo() {
		return this.traceInfo;
	}

	public void setTraceInfo(TraceInfo traceInfo) {
		this.traceInfo = traceInfo;
	}

	public void setParamWithoutValidate(String[] params) {
		this.paramWithoutValidate = params;
	}

	public String[] getParamWithoutValidate() {
		return this.paramWithoutValidate;
	}

}
