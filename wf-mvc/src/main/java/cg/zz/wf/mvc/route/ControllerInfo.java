package cg.zz.wf.mvc.route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import cg.zz.wf.mvc.MvcController;
import cg.zz.wf.mvc.annotation.GET;
import cg.zz.wf.mvc.annotation.Ignored;
import cg.zz.wf.mvc.annotation.POST;
import cg.zz.wf.mvc.annotation.Path;

/**
 * 
 * Controller的类信息
 * 
 * @author chengang
 *
 */
class ControllerInfo {

	/**
	 * key=Controller Bean名称，Value=ControllerInfo
	 */
	static HashMap<String, ControllerInfo> classMaps = new LinkedHashMap<>();
	
	/**
	 * Controller对象实体
	 */
	public Object controller;
	
	/**
	 * 本Controller的根Path
	 */
	public Path path = null;
	
	/**
	 * 是否支持Get
	 */
	public boolean isGet = false;
	
	/**
	 * 是否支持Post
	 */
	public boolean isPost = false;
	
	/**
	 * 维护的具体的请求处理的ActionInfo集合
	 */
	public Set<ActionInfo> actions = new LinkedHashSet<>();
	
	/**
	 * Controller类上所有的注解集合
	 */
	public Set<Annotation> annotations = new LinkedHashSet<>();

	/**
	 * 返回维护的ActionInfo数量
	 * @return int
	 */
	public int size() {
		return this.actions.size();
	}

	/**
	 * Controller是否有Path注解
	 * @return boolean
	 */
	public boolean hasTypePath() {
		return this.path != null;
	}

	/**
	 * 返回Path注解的值
	 * @return String[]
	 */
	public String[] getUrlTypeLevelPatterns() {
		return this.path == null ? new String[0] : this.path.value();
	}

	/**
	 * 构造并初始化ControllerInfo对象的属性等
	 * @param beanName - Controller Bean名称
	 * @param context - ApplicationContext
	 */
	private ControllerInfo(String beanName, ApplicationContext context) {
		//根据Bean名称获取Class
		Class<?> handlerType = context.getType(beanName);
		//获取Path注解
		this.path = AnnotationUtils.findAnnotation(handlerType, Path.class);
		//全局是否是get请求
		this.isGet = (AnnotationUtils.findAnnotation(handlerType, GET.class) != null);
		//全局是否是post请求
		this.isPost = (AnnotationUtils.findAnnotation(handlerType, POST.class) != null);
		if (!this.isGet && !this.isPost) {
			this.isGet = true;
			this.isPost = true;
		}
		this.controller = context.getBean(beanName);
		//维护Controller类上所有的注解
		for (Annotation ann : handlerType.getAnnotations()) {
            this.annotations.add(ann);
        }
		
		//扫描指定类的所有方法，并将符合条件的方法转换成ActionInfo存储到actions集合中
		ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback() {
			
			public void doWith(Method method) {
				ActionInfo action = ActionInfo.Factory(ControllerInfo.this, method);
				ControllerInfo.this.actions.add(action);
			}
			
		}, RouteUtils.HANDLER_METHODS);
	}

	/**
	 * 根据Controller Bean名称初始化ControllerInfo对象
	 * @param beanName - Controller Bean名称
	 * @param context - ApplicationContext
	 * @return ControllerInfo
	 */
	public static ControllerInfo Factory(String beanName, ApplicationContext context) {
		Class<?> handlerType = context.getType(beanName);
		ControllerInfo c = (ControllerInfo) classMaps.get(beanName);
		if (c != null) {
			return c;
		}
		//检查类名称是否是以Controller结尾
		if (!handlerType.getName().endsWith("Controller")) {
			return null;
		}
		//检查类是否是继承MvcController抽象类
		if (!ClassUtils.isAssignable(MvcController.class, handlerType)) {
			return null;
		}
		String packageName = ClassUtils.getPackageName(handlerType);
		//如果当前类的包名是否以cg.zz开头，结尾是否以controllers结束
		if (!packageName.startsWith("cg.zz.") || !packageName.endsWith("controllers")) {
			return null;
		}
		//检查是否被注解了Ignored注解
		if (AnnotationUtils.findAnnotation(handlerType, Ignored.class) != null) {
			return null;
		}
		//初始化ControllerInfo对象
		ControllerInfo ce = new ControllerInfo(beanName, context);
		classMaps.put(beanName, ce);

		return ce;
	}

}
