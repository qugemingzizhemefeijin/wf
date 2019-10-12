package cg.zz.wf.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

/**
 * 
 * 如果MvcDispatcher无法处理，则直接转到此处理器上，主要用于处理静态资源文件
 * 
 * @author chengang
 *
 */
public class StaticFileHandler {

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURI().substring(request.getContextPath().length());
		if (url.toUpperCase().startsWith("/WEB-INF/")) {
			response.sendError(HttpStatus.NOT_FOUND.value());
			return;
		}
		//匹配出资源路径
		int n = url.indexOf('?');
	    if (n != -1) {
	      url = url.substring(0, n);
	    }
	    n = url.indexOf('#');
	    if (n != -1) {
	      url = url.substring(0, n);
	    }
	    String path = "/resources" + url;
	    request.getRequestDispatcher(path).forward(request, response);
	}

}
