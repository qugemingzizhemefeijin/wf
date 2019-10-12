package cg.zz.wf.mvc.trace;

/**
 * 
 * Request跟踪对象
 * 
 * @author chengang
 *
 */
public class RequestTraceInfo {

	/**
	 * sessionId
	 */
	private String sessionId;
	
	/**
	 * 请求类型
	 */
	private String reqType;
	
	/**
	 * 请求时间
	 */
	private String reqTime;
	
	/**
	 * 请求编码
	 */
	private String reqEncoding;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 输出编码
	 */
	private String resEncoding;
	
	/**
	 * header头信息
	 */
	private String header;

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getReqType() {
		return this.reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReqTime() {
		return this.reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String getReqEncoding() {
		return this.reqEncoding;
	}

	public void setReqEncoding(String reqEncoding) {
		this.reqEncoding = reqEncoding;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResEncoding() {
		return this.resEncoding;
	}

	public void setResEncoding(String resEncoding) {
		this.resEncoding = resEncoding;
	}

	public String getHeader() {
		return this.header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

}
