package cg.zz.wf.mvc.client;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.server.SessionHandler;

/**
 * 
 * 客户环境
 * 
 * @author chengang
 *
 */
public class ClientContext {

	final BeatContext beat;
	CookieHandler cookie;
	SessionHandler session;

	public ClientContext(BeatContext beat) {
		this.beat = beat;
	}

	/**
	 * 获得cookie处理器
	 * @return CookieHandler
	 */
	public CookieHandler getCookies() {
		if (this.cookie == null) {
			this.cookie = new CookieHandler(this.beat);
		}
		return this.cookie;
	}

	/**
	 * 获得UploadRequest，只有在文件表单提交的时候才不为空
	 * @return UploadRequest
	 */
	public UploadRequest getUploads() {
		HttpServletRequest request = this.beat.getRequest();
		return (request instanceof UploadRequest) ? (UploadRequest) this.beat.getRequest() : null;
	}

	/**
	 * 判断当前的request是否是文件上传的请求
	 * @return boolean
	 */
	public boolean isUpload() {
		return getUploads() != null;
	}

	/**
	 * 获得相对的URL路径
	 * @return String
	 */
	public String getRelativeUrl() {
		HttpServletRequest request = this.beat.getRequest();
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String relativeUrl = uri.substring(contextPath.length());
		if (Objects.equals("/index.jsp", relativeUrl)) {
			relativeUrl = "/";
		}
		return relativeUrl;
	}

}
