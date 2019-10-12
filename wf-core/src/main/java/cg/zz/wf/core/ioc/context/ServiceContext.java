package cg.zz.wf.core.ioc.context;

/**
 * 
 * 服务环境
 * 
 * @author chengang
 *
 */
public interface ServiceContext {

	/**
	 * 根据名称获取对象
	 * @param name - 名称
	 * @return Object
	 */
	public Object getBean(String name);
	
	/**
	 * 根据类型获取对象
	 * @param requiredType - Class<T>
	 * @return T
	 */
	public <T> T getBean(Class<T> requiredType);
	
	/**
	 * 根据类型和名称获取对象
	 * @param name - 名称
	 * @param requiredType - Class<T>
	 * @return T
	 */
	public <T> T getBean(String name, Class<T> requiredType);

	/**
	 * 判断在服务中是否有指定名称的对象
	 * @param name - String
	 * @return boolean
	 */
	public boolean containsBean(String name);

	/**
	 * 判断在服务中指定名称的对象是否是单例
	 * @param name - String
	 * @return boolean
	 */
	public boolean isSingleton(String name);

}
