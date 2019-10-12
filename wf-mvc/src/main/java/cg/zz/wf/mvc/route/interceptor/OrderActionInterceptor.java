package cg.zz.wf.mvc.route.interceptor;

import cg.zz.wf.mvc.interceptor.ActionInterceptor;

/**
 * 
 * 拦截器对象维护的实体
 * 
 * @author chengang
 *
 */
class OrderActionInterceptor {

	/**
	 * 顺序值
	 */
	int order;
	
	/**
	 * 拦截器
	 */
	ActionInterceptor interceptor;

	public OrderActionInterceptor(int order, ActionInterceptor interceptor) {
		this.order = order;
		this.interceptor = interceptor;
	}

}
