package cg.zz.wf.mvc.route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import cg.zz.wf.mvc.ActionAttribute;
import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.annotation.GET;
import cg.zz.wf.mvc.annotation.POST;
import cg.zz.wf.mvc.annotation.Path;
import cg.zz.wf.mvc.route.interceptor.InterceptorHandler;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 
 * Controller类具体的请求方法信息
 * 
 * @author chengang
 *
 */
public class ActionInfo implements ActionAttribute {

	/**
	 * ActionInfo对应的Method方法对象
	 */
	private Method actionMethod;
	
	/**
	 * Method上所有的注解+所属类上的所有的注解
	 */
	private Set<Annotation> annotations = new LinkedHashSet<>();
	
	/**
	 * ControllerInfo对象
	 */
	private ControllerInfo controllerInfo;
	
	/**
	 * 拦截器处理和维护类实体
	 */
	private InterceptorHandler interceptorHandler = new InterceptorHandler();
	
	/**
	 * 是否支持Get请求
	 */
	private boolean isGet = false;
	
	/**
	 * 是否支持Post请求
	 */
	private boolean isPost = false;
	
	/**
	 * URL Path 集合
	 */
	private String[] mappedPatterns = new String[0];
	
	/**
	 * 方法参数名称集合
	 */
	private String[] paramNames = new String[0];
	
	/**
	 * 方法参数类型集合
	 */
	Class<?>[] paramTypes = new Class[0];
	
	/**
	 * URL正则表达式匹配器
	 */
	private PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 构造并初始化ActionInfo
	 * @param controller - ControllerInfo
	 * @param method - 方法对象
	 */
	private ActionInfo(ControllerInfo controller, Method method) {
		this.controllerInfo = controller;
		this.actionMethod = method;

		//查找方法是否有Path注解
		Path path = AnnotationUtils.findAnnotation(method, Path.class);
		//如果没有path注解，则以方法名为path的值
		String[] pathPatterns = path != null ? path.value() : new String[]{method.getName()};

		Set<String> pathUrls = new LinkedHashSet<>();
        for (String mappedPattern : pathPatterns) {
        	//如果Controller没有Path注解，并且mappedPattern空或者首字符不是'/'，则需要在之前添加上'/'字符
            if (!controller.hasTypePath() && (mappedPattern.length() == 0 || mappedPattern.charAt(0) != '/')) {
                mappedPattern = "/" + mappedPattern;
            }
            pathUrls.add(mappedPattern);
        }
        
        //组合Path URL
		buildUrlsByController(pathUrls);

		//是否支持GET请求
		this.isGet = (AnnotationUtils.findAnnotation(method, GET.class) != null);

		//是否支持POST请求
		this.isPost = (AnnotationUtils.findAnnotation(method, POST.class) != null);
		//如果不支持GET和POST则以Controller的支持类型为准
		if (!this.isGet && !this.isPost) {
			this.isGet = controller.isGet;
			this.isPost = controller.isPost;
		}
		//获得方法参数的类型集合
		this.paramTypes = method.getParameterTypes();
		//获得方法参数的名字集合
		this.paramNames = getMethodParamNames();
		
		//将Controller上注解和方法上的注解都添加到annotations集合中
		for (Annotation ann : controller.annotations) {
            this.annotations.add(ann);
        }
        for (Annotation ann : method.getAnnotations()) {
            this.annotations.add(ann);
        }
        //初始化其支持的拦截器对象
		this.interceptorHandler.build(this);
	}
	
	/**
	 * 创建并初始化ActionInfo
	 * @param controller - ControllerInfo
	 * @param method - Method
	 * @return ActionInfo
	 */
	public static ActionInfo Factory(ControllerInfo controller, Method method) {
		return new ActionInfo(controller, method);
	}

	/**
	 * 创建URL Pattern 集合，包括跟ControllerInfo组合成新的Path URL
	 * @param pathUrls - Set<String>
	 */
	private void buildUrlsByController(Set<String> pathUrls) {
		//获取Controller的Path值
		String[] controllerPatterns = this.controllerInfo.getUrlTypeLevelPatterns();
		Set<String> urlPatterns = new LinkedHashSet<>();
		for (String controllerPattern : controllerPatterns) {
			//如果Controller的URL Pattern是空或者前缀不是以'/'开否，则添加上'/'
			if (controllerPattern.length() == 0 || controllerPattern.charAt(0) != '/') {
				controllerPattern = "/" + controllerPattern;
			}
			//将Controller的Path值喝Method的Path值合并到一起，并维护到urlPatterns集合中
			for (String methodLevelPattern : pathUrls) {
				addUrlsForPath(urlPatterns, this.pathMatcher.combine(controllerPattern, methodLevelPattern));
			}
		}
		//如果Controller没有Path，则单独将Method的Path值维护到urlPatterns集合中
		if (controllerPatterns.length == 0) {
			for (String methodLevelPattern2 : pathUrls) {
				if (methodLevelPattern2.length() == 0 || methodLevelPattern2.charAt(0) != '/') {
					methodLevelPattern2 = "/" + methodLevelPattern2;
				}
				addUrlsForPath(urlPatterns, methodLevelPattern2);
			}
		}
		
		this.mappedPatterns = urlPatterns.toArray(new String[urlPatterns.size()]);
		
		//打印Path信息到控制台
		System.out.println(">>mappedPatterns : " + this.actionMethod);
		for(String path : this.mappedPatterns) {
			System.out.println("...  " + path);
		}
	}

	/**
	 * 将Path添加到urls集合中，并且添加额外的.*和/的后缀的URL
	 * @param urls - Set<String>
	 * @param path - URL Path
	 */
	private void addUrlsForPath(Set<String> urls, String path) {
		urls.add(path);
		if (path.indexOf('.') == -1 && !path.endsWith("/")) {
			urls.add(path + ".*");
			urls.add(path + "/");
		}
	}

	/**
	 * 通过javassist读取局部变量表中的参数名称
	 * @return String[]
	 */
	private String[] getMethodParamNames() {
		Class<?> clazz = this.controllerInfo.controller.getClass();
		Method method = this.actionMethod;
		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(clazz));
			CtClass cc = pool.get(clazz.getName());
			String[] paramTypeNames = new String[method.getParameterTypes().length];
			for (int i = 0; i < this.paramTypes.length; i++) {
				paramTypeNames[i] = this.paramTypes[i].getName();
			}
			CtMethod cm = cc.getDeclaredMethod(method.getName(), pool.get(paramTypeNames));
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute("LocalVariableTable");
			if (attr == null) {
				throw new RuntimeException("class:" + clazz.getName() + ", have no LocalVariableTable, please use javac -g:{vars} to compile the source file");
			}
			int startIndex = getStartIndex(attr);
			String[] paramNames2 = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for (int i2 = 0; i2 < paramNames2.length; i2++) {
				paramNames2[i2] = attr.variableName(startIndex + i2 + pos);
			}
			for (String println : paramNames2) {
				System.out.println(println);
			}
			return paramNames2;
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new String[0];
		}
	}

	private int getStartIndex(LocalVariableAttribute attr) {
		for (int i = 0; i < attr.length(); i++) {
			if ("this".equals(attr.variableName(i))) {
				return i;
			}
		}
		return 0;
	}

	public Set<Annotation> getAnnotations() {
		return this.annotations;
	}

	public InterceptorHandler getInterceptorHandler() {
		return this.interceptorHandler;
	}

	public Class<?>[] getParamTypes() {
		return this.paramTypes;
	}

	public String[] getParamNames() {
		return this.paramNames;
	}

	public Object getController() {
		return this.controllerInfo.controller;
	}

	public void setController(ControllerInfo controller) {
		this.controllerInfo = controller;
	}

	public Method getActionMethod() {
		return this.actionMethod;
	}

	public boolean isGet() {
		return this.isGet;
	}

	public boolean isPost() {
		return this.isPost;
	}

	public String[] getMappedPatterns() {
		return this.mappedPatterns;
	}

	public boolean matchRequestMethod(BeatContext beat) {
		String requestMethod = beat.getRequest().getMethod();
		if (StringUtils.equalsIgnoreCase(requestMethod, "GET") && !this.isGet) {
			return false;
		}
		if (!StringUtils.equalsIgnoreCase(requestMethod, "POST") || this.isPost) {
			return true;
		}
		return false;
	}

}
