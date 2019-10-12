package cg.zz.wf.mvc;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cg.zz.wf.mvc.thread.BeatContextBean;
import cg.zz.wf.mvc.trace.CustomTraceInfo;
import cg.zz.wf.mvc.trace.RequestTraceInfo;
import cg.zz.wf.mvc.trace.ResponseTraceInfo;
import cg.zz.wf.mvc.trace.TraceInfo;

public class Trace {

	private static final String customTraceTitle = "<table width='100%'><tr colspan='2'  bgcolor='#333366'>CUSTOM TRACE INFO : </tr><tr><td>CONTENT</td><td>WASTETIME</td></tr>";
	private static final String sysTraceTitle = "<table width='100%'><tr colspan='2'  bgcolor='#333366'>SYS TRACE INFO : </tr><tr><td>TITLE</td><td>CONTENT</td></tr>";
	private static final String tableEnd = "</table>";
	//这个线程不安全
	private static final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 记录自定义跟踪点
	 * @param content - 自定义描述
	 * @param beat - BeatContext
	 */
	public static void trace(String content, BeatContext beat) {
		TraceInfo traceInfo = beat.getTraceInfo();
		if (traceInfo == null) {
			return;
		}
		
		long curTime = System.currentTimeMillis();
		
		//生成一个跟踪记录对象
		CustomTraceInfo cti = new CustomTraceInfo();
		cti.setContent(content);
		cti.setWasteTime(curTime - traceInfo.getLastTraceTime());
		
		//重置上次跟踪时间
		traceInfo.setLastTraceTime(curTime);
		//将对象放到自定义跟踪列表中
		traceInfo.getCustomTraceInfos().add(cti);
	}

	/**
	 * 初始化，请求必须带trace+当前时间才会执行跟踪逻辑
	 * @param beat
	 */
	public static void init(BeatContextBean beat) {
		String trace = beat.getRequest().getParameter("trace" + sdf.format(new Date()));
		if (trace == null || !trace.equals("true")) {
			return;
		}
		
		//创建一个跟踪对象
		TraceInfo traceInfo = new TraceInfo();
		traceInfo.setArriveTime(System.currentTimeMillis());
		traceInfo.setLastTraceTime(System.currentTimeMillis());
		traceInfo.setCustomTraceInfos(new ArrayList<>());
		traceInfo.setReqInfo(new RequestTraceInfo());
		traceInfo.setResInfo(new ResponseTraceInfo());
		
		//将跟踪信息设置到BeanContext中
		beat.setTraceInfo(traceInfo);
	}

	/**
	 * 输出当前跟踪的信息到页面上
	 * @param beat - BeatContext
	 * @throws IOException
	 */
	public static void wrapper(BeatContext beat) throws IOException {
		if (beat.getTraceInfo() == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		//输出自定义跟踪的信息
		getCustomInfo(sb, beat);
		//输出系统跟踪的信息，好像没用上
		getSysInfo(sb, beat);
		beat.getResponse().getWriter().write(sb.toString());
	}

	/**
	 * 生成自定义跟踪点的Table表格
	 * @param sb - StringBuilder
	 * @param beat - BeatContext
	 */
	private static void getCustomInfo(StringBuilder sb, BeatContext beat) {
		sb.append(customTraceTitle);
		for (CustomTraceInfo cti : beat.getTraceInfo().getCustomTraceInfos()) {
			sb.append("<tr>");
			sb.append("<td>" + cti.getContent() + "</td>");
			sb.append("<td>" + cti.getWasteTime() + "</td>");
			sb.append("</tr>");
		}
		sb.append(tableEnd);
	}

	/**
	 * 生成系统跟踪点的Table表格
	 * @param sb - StringBuilder
	 * @param beat - BeatContext
	 */
	private static void getSysInfo(StringBuilder sb, BeatContext beat) {
		sb.append(sysTraceTitle);
		sb.append(tableEnd);
	}

}
