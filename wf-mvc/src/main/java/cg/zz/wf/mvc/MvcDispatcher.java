package cg.zz.wf.mvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import cg.zz.wf.core.WF;
import cg.zz.wf.mvc.bind.BindAndValidate;
import cg.zz.wf.mvc.context.MvcWebAppContext;
import cg.zz.wf.mvc.invoke.ActionInvoker;
import cg.zz.wf.mvc.route.BeatRouter;
import cg.zz.wf.mvc.route.RouteResult;
import cg.zz.wf.mvc.route.RouterBuilder;
import cg.zz.wf.mvc.thread.BeatContextBean;
import cg.zz.wf.mvc.thread.BeatContextUtils;
import cg.zz.wf.mvc.view.VelocityTemplateFactory;

/**
 * 
 * MVC请求派发器
 * 
 * @author chengang
 *
 */
class MvcDispatcher {

	//方法调用者
	private ActionInvoker handler = new ActionInvoker();
	
	/**
	 * 请求的路由规则维护
	 */
	private BeatRouter router = null;
	
	private ServletContext sc;

	public MvcDispatcher(ServletContext sc) throws ServletException {
		this.sc = sc;

		System.out.println("Starting Mvc Webapplication ...");
		System.out.println("namespace:" + WF.getNamespace());

		//初始化Velocity
		VelocityTemplateFactory.init(sc);

		//初始化spring application
		MvcWebAppContext context = initWebApplicationtext();

		//重新刷新application
		context.refresh();

		BindAndValidate.setApplicationContext(context);

		//这里会初始化并维护所有的URL Path，并创建BeatRouter路由类，用于查找符合URL请求规则的Method并反射调用
		this.router = RouterBuilder.build(context);

		BeatContextBean.servletContext = sc;
	}

	public boolean service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getCharacterEncoding() == null) {
			request.setCharacterEncoding("UTF-8");
		}
		//创建并绑定BeatContext到本地线程变量中
		BeatContext beat = BeatContextUtils.BeatContextWapper(request, response);

		//匹配符合条件的处理方法
		RouteResult match = this.router.route(beat);
		if (match == null) {
			return removeCurrentThread(false);
		}
		((BeatContextBean) beat).setAction(match.action);

		//反射调用执行方法
		ActionResult actionResult = (ActionResult)this.handler.invoke(match);

		//如果处理的结果为空，直接返回并移除BeatContext
		if (actionResult == null) {
			return removeCurrentThread(false);
		}
		actionResult.render(beat);

		//将当前跟踪的信息追加输出到页面上，如果请求没有带trace+当前时间则不会执行
		Trace.wrapper(beat);

		//将结果缓存起来，这里如果请求的方法没有添加OutputCache注解，实际就不会被执行缓存策略
		beat.getCache().setCacheResult();

		//最后移除BeatContext
		return removeCurrentThread(true);
	}

	public void destroy() {
		System.out.println("end Mvc WebApplication");
	}

	/**
	 * 移除当前线程绑定的BeatContext
	 * @param result - boolean
	 * @return boolean
	 */
	private boolean removeCurrentThread(boolean result) {
		BeatContextUtils.remove();
		return result;
	}

	/**
	 * 初始化Spring Application
	 * @return MvcWebAppContext
	 */
	private MvcWebAppContext initWebApplicationtext() {
		ApplicationContext oldRootContext = (ApplicationContext) this.sc.getAttribute(MvcConstants.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		if (oldRootContext != null) {
			if (oldRootContext.getClass() != MvcWebAppContext.class) {
				throw new IllegalStateException("Cannot initialize context because there is already a root application context present - check whether you have multiple ContextLoader* definitions in your web.xml!");
			}
			return (MvcWebAppContext) oldRootContext;
		}
		MvcWebAppContext context = new MvcWebAppContext(this.sc, false);

		String contextConfigLocation = MvcConstants.DEFAULT_CONFIG_LOCATION;
		System.out.println("-------------contextConfigLocation------------" + contextConfigLocation);
		context.setConfigLocation(contextConfigLocation);
		context.setId("cg.zz.mvcapplicationcontext");
		
		System.out.println("----MvcWebAppContext----" + context);
		this.sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
		
		return context;
	}
}
