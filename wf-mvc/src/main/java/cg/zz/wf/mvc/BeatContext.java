package cg.zz.wf.mvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cg.zz.wf.mvc.bind.BeatBindResults;
import cg.zz.wf.mvc.cache.CacheContext;
import cg.zz.wf.mvc.client.ClientContext;
import cg.zz.wf.mvc.server.ServerContext;
import cg.zz.wf.mvc.trace.TraceInfo;

/**
 * 
 * 本次请求的上下文接口，具体由BeatContextBean对象实现
 * 
 * @author chengang
 *
 */
public interface BeatContext {

	/**
	 * 获得Model变量绑定对象
	 * @return Model
	 */
	public Model getModel();

	/**
	 * 获得HttpServletRequest，此处肯定是封装后的Request对象
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest();

	/**
	 * 获得HttpServletResponse，此处有可能是封装后的Response对象
	 * @return HttpServletResponse
	 */
	public HttpServletResponse getResponse();

	/**
	 * 获得Http ServletContext
	 * @return ServletContext
	 */
	public ServletContext getServletContext();

	/**
	 * 获得参数验证信息的结果对象
	 * @return BeatBindResults
	 */
	public BeatBindResults getBindResults();

	/**
	 * 获得客户端上下文对象
	 * @return ClientContext
	 */
	public ClientContext getClient();

	/**
	 * 获得服务器上下文对象
	 * @return ServerContext
	 */
	public ServerContext getServer();

	public ActionAttribute getAction();
	
	/**
	 * 获得请求的相对地址
	 * @return String
	 */
	@Deprecated
    String getRelativeUrl();

	/**
	 * 获取缓存相关上下文对象
	 * @return CacheContext
	 */
	public CacheContext getCache();

	/**
	 * 获得跟踪记录对象
	 * @return TraceInfo
	 */
	public TraceInfo getTraceInfo();

	/**
	 * 设置不需要进行参数值转换的参数名称数组
	 * @param params - String[]
	 */
	public void setParamWithoutValidate(String[] params);

	/**
	 * 获得不需要进行参数值转换的参数名称数组
	 * @return String[]
	 */
	public String[] getParamWithoutValidate();

}
