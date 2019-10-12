package cg.zz.wf.mvc.view;

import javax.servlet.ServletContext;

import org.apache.velocity.app.Velocity;

/**
 * 
 * 初始化Velocity模版的信息
 * 
 * @author chengang
 *
 */
public class VelocityTemplateFactory {

	public static void init(ServletContext sc) {
		String webAppPath = sc.getRealPath("/");
		
		Velocity.setProperty("resource.loader", "file");
		Velocity.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		Velocity.setProperty("file.resource.loader.path", webAppPath);
		Velocity.setProperty("file.resource.loader.cache", "false");
		Velocity.setProperty("file.resource.loader.modificationCheckInterval", "2");
		Velocity.setProperty("input.encoding", "UTF-8");
		Velocity.setProperty("output.encoding", "UTF-8");
		Velocity.setProperty("default.contentType", "text/html; charset=UTF-8");
		Velocity.setProperty("velocimarco.library.autoreload", "true");
		Velocity.setProperty("runtime.log.error.stacktrace", "false");
		Velocity.setProperty("runtime.log.warn.stacktrace", "false");
		Velocity.setProperty("runtime.log.info.stacktrace", "false");
		Velocity.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		Velocity.setProperty("runtime.log.logsystem.log4j.category", "velocity_log");
		try {
			Velocity.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private VelocityTemplateFactory() {

	}

}
