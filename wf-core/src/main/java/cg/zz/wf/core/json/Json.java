package cg.zz.wf.core.json;

/**
 * 
 * JSON接口
 * 
 * @author chengang
 *
 */
public interface Json {

	/**
	 * 将字符串转换为指定的对象
	 * @param s - JSON字符串
	 * @param cls - Class
	 * @return T
	 */
	public <T> T fromJson(String s, Class<T> cls);

	/**
	 * 将对象转换成JSON字符串
	 * @param o - Object
	 * @return String
	 */
	public String toJson(Object o);

	/**
	 * 设置日期格式
	 * @param pattern - 日期格式
	 */
	public void setDateFormat(String pattern);

	/**
	 * 获得JSON Mapper转换器
	 * @return Object
	 */
	public Object getMapper();

}
