package cg.zz.wf.mvc.thread;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 维护BeatContext在本地线程变量的工具类
 * 
 * @author chengang
 *
 */
public final class BeatContextUtils {

	static ThreadLocal<BeatContext> beatThreadLocal = new ThreadLocal<>();

	/**
	 * 此方法应该是用于手动来关联一个BeatContext
	 * @param beat - BeatContext
	 */
	public static void bindBeatContextToCurrentThread(BeatContextLocal beat) {
		beatThreadLocal.set(beat);
	}

	/**
	 * 获得当前请求线程的BeatContext
	 * @return BeatContext
	 */
	public static BeatContext getCurrent() {
		BeatContext beat = (BeatContext) beatThreadLocal.get();
		if (beat == null) {
			throw new IllegalStateException("BeatContext");
		}
		return beat;
	}

	/**
	 * 创建当前请求的BeatContext并维护到线程变量中
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @return BeatContext
	 */
	public static BeatContext BeatContextWapper(HttpServletRequest request, HttpServletResponse response) {
		BeatContext beat = new BeatContextBean(request, response);
		beat.getModel().add("__beat", beat);
		beatThreadLocal.set(beat);

		return beat;
	}

	/**
	 * 从线程变量中移除BeatContext
	 */
	public static void remove() {
		beatThreadLocal.remove();
	}

	private BeatContextUtils() {

	}

}
