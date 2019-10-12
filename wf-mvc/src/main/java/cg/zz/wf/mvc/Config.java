package cg.zz.wf.mvc;

import javax.servlet.ServletContext;

public interface Config {

	public ServletContext getServletContext();

	public String getInitParameter(String str);

}
