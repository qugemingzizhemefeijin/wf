package cg.zz.wf.mvc;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import cg.zz.wf.core.WF;
import cg.zz.wf.core.log.Log;
import cg.zz.wf.core.log.LogFactory;

/**
 * 
 * WF的拦截器，用于专门做请求派发
 * 
 * @author chengang
 *
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, initParams = { @WebInitParam(name = "encoding", value = "UTF-8") }, urlPatterns = { "/*" })
public class MvcFilter implements Filter {
	
	protected static final Log log = LogFactory.getLog(MvcFilter.class);
	
	/**
	 * URL请求派发器
	 */
	private static MvcDispatcher dispatcher;
	
	/**
	 * 是否已经被初始化了
	 */
	private static volatile boolean hasInitial = false;
	
	/**
	 * 静态资源处理器
	 */
	private static StaticFileHandler staticFileHandler = new StaticFileHandler();
	private static ServletContext sc;

	@Override
	public void init(FilterConfig config) throws ServletException {
		System.out.println("Filter:" + this);
		synchronized (MvcFilter.class) {
			if (!hasInitial) {
				hasInitial = true;
				log.info("Mvc WebApplication initialing");
				System.out.println(WF.getNamespace() + ": initialing ...");
				sc = config.getServletContext();
				dispatcher = new MvcDispatcher(getServletContext());
				log.info("Startted Mvc WebApplication");
				System.out.println(WF.getNamespace() + ": Startted Mvc WebApplication");
			}

		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;
		
		String method = httpReq.getMethod();
		if ("GET".equals(method) || "POST".equals(method)) {
			try {
				if (!dispatcher.service(httpReq, httpResp)) {
					staticFileHandler.handle(httpReq, httpResp);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				//e.printStackTrace();
				httpResp.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
			}
		} else {
			httpResp.sendError(HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public void destroy() {
		System.out.println("destroy filter:" + this);
	}
	
	private ServletContext getServletContext() {
        return sc;
    }

}
