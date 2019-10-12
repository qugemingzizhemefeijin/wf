package cg.zz.wf.mvc;

import org.springframework.web.context.WebApplicationContext;

import cg.zz.wf.mvc.context.MvcWebAppContext;

/**
 * 
 * MVC常量
 * 
 * @author chengang
 *
 */
public class MvcConstants {
	
	public static final String APPLICATION_CONTEXT_ID = MvcWebAppContext.class.getName() + "_CONTEXT_ID";
	
	/**
	 * 默认的配置文件路径
	 */
    public static final String DEFAULT_CONFIG_LOCATION = "classpath*:MvcApplicationContext.xml";
    
    /**
     * 资源目录
     */
    public static final String RESOURCES_PREFIX = "/resources";
    
    /**
     * Spring ApplicationContext 设置在ServletContext中的变量名称
     */
    public static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
    
    /**
     * Velocity模版文件的后缀
     */
    public static final String VIEW_ENGINE = "vm";
    
    /**
     * Velocity模版文件的前缀
     */
    public static final String VIEW_PREFIX = "views";
    
    private MvcConstants() {
    	
    }

}
