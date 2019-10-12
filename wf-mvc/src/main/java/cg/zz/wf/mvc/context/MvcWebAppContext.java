package cg.zz.wf.mvc.context;

import javax.servlet.ServletContext;

import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 
 * Spring Web Application<br/>
 * 主要就是设置了一下ServletContext
 * 
 * @author chengang
 *
 */
public class MvcWebAppContext extends XmlWebApplicationContext {

	public MvcWebAppContext(ServletContext servletContext, boolean refresh) {
		setServletContext(servletContext);
		if (refresh) {
			refresh();
		}
	}

}
