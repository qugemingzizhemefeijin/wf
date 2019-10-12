package cg.zz.wf.mvc.trace;

/**
 * 
 * 自定义跟踪对象
 * 
 * @author chengang
 *
 */
public class CustomTraceInfo {

	/**
	 * 耗时时间，毫秒
	 */
	private long wasteTime;
	
	/**
	 * 跟踪描述信息
	 */
	private String content;

	public long getWasteTime() {
		return this.wasteTime;
	}

	public void setWasteTime(long wasteTime) {
		this.wasteTime = wasteTime;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
