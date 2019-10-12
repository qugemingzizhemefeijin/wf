package cg.zz.wf.mvc.trace;

import java.util.List;

public class TraceInfo {

	/**
	 * 到达时间
	 */
	private long arriveTime;
	
	/**
	 * 上次跟踪时间
	 */
	private long lastTraceTime;
	
	/**
	 * 离开时间
	 */
	private long leaveTime;
	
	/**
	 * Request跟踪
	 */
	private RequestTraceInfo reqInfo;
	
	/**
	 * Response跟踪
	 */
	private ResponseTraceInfo resInfo;
	
	/**
	 * 自定义跟踪
	 */
	private List<CustomTraceInfo> customTraceInfos;

	public RequestTraceInfo getReqInfo() {
		return this.reqInfo;
	}

	public void setReqInfo(RequestTraceInfo reqInfo) {
		this.reqInfo = reqInfo;
	}

	public ResponseTraceInfo getResInfo() {
		return this.resInfo;
	}

	public void setResInfo(ResponseTraceInfo resInfo) {
		this.resInfo = resInfo;
	}

	public long getArriveTime() {
		return this.arriveTime;
	}

	public void setArriveTime(long arriveTime) {
		this.arriveTime = arriveTime;
	}

	public long getLastTraceTime() {
		return this.lastTraceTime;
	}

	public void setLastTraceTime(long lastTraceTime) {
		this.lastTraceTime = lastTraceTime;
	}

	public long getLeaveTime() {
		return this.leaveTime;
	}

	public void setLeaveTime(long leaveTime) {
		this.leaveTime = leaveTime;
	}

	public List<CustomTraceInfo> getCustomTraceInfos() {
		return this.customTraceInfos;
	}

	public void setCustomTraceInfos(List<CustomTraceInfo> customTraceInfos) {
		this.customTraceInfos = customTraceInfos;
	}

}
