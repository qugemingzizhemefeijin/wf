package cg.zz.wf.core.ioc.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 * Spring环境类
 * 
 * @author chengang
 *
 */
public class SpringApplicationContext implements ServiceContext {
	
	private static ApplicationContext context;
	
	public SpringApplicationContext(String configLocation) {
		if (context != null) {
			return;
		}
		context = new FileSystemXmlApplicationContext("file:" + configLocation);
	}

	@Override
	public Object getBean(String name) {
		return context.getBean(name);
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) {
		return (T) context.getBean(name, requiredType);
	}

	@Override
	public <T> T getBean(Class<T> requiredType) {
		return (T) context.getBean(requiredType);
	}
	
	public Object getBean(String name, Object... args) {
		return context.getBean(name, args);
	}

	@Override
	public boolean containsBean(String name) {
		return context.containsBean(name);
	}

	@Override
	public boolean isSingleton(String name) {
		return context.isSingleton(name);
	}

}
