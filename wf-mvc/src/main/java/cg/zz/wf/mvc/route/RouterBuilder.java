package cg.zz.wf.mvc.route;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import cg.zz.wf.mvc.MvcController;

/**
 * 
 * 请求路由构造器
 * 
 * @author chengang
 *
 */
public class RouterBuilder {

	/**
	 * 构造请求的路由维护对象
	 * @param context - ApplicationContext
	 * @return BeatRouter
	 */
	public static BeatRouter build(ApplicationContext context) {
		return detectActionExecutions(context);
	}

	/**
	 * 通过Spring获取所有继承了MvcController类的bean名称，并构造ControllerInfo维护到BeatRouter对象中
	 * @param context - ApplicationContext
	 * @return BeatRouter
	 */
	private static BeatRouter detectActionExecutions(ApplicationContext context) {
		Set<ActionInfo> actions = new LinkedHashSet<>();

		//循环迭代继承了MvcController类的实例
		for (String beanName : context.getBeanNamesForType(MvcController.class)) {
            System.out.println("beanName : " + beanName);
            //构造ControllerInfo
            ControllerInfo ce = ControllerInfo.Factory(beanName, context);
            if (ce != null) {
                addActionExecution(actions, ce);
            }
        }
		//构造BeatRouter对象
		return BuildMappingGroup(actions);
	}

	/**
	 * 将ControllerInfo中ActionInfo都添加到actions集合中
	 * @param actions - Set<ActionInfo>
	 * @param ce - ControllerInfo
	 */
	private static void addActionExecution(Set<ActionInfo> actions, ControllerInfo ce) {
		actions.addAll(ce.actions);
	}

	/**
	 * 创建BeatRouter对象并将所有的actions集合添加此对象中
	 * @param actions - Set<ActionInfo>
	 * @return BeatRouter
	 */
	private static BeatRouter BuildMappingGroup(Set<ActionInfo> actions) {
		BeatRouter router = new BeatRouter();
		for (ActionInfo action : actions) {
			router.addMapping(action);
		}
		return router;
	}

}
