package cg.zz.wf.mvc.server;

import java.util.Enumeration;

/**
 * 
 * 服务器端Session处理器
 * 
 * @author chengang
 *
 */
public interface SessionHandler {

	/**
	 * 根据session名称获得对象
	 * @param name - 名称
	 * @return Object
	 */
	public Object get(String name);

	/**
	 * 获得Session创建的时间
	 * @return Long
	 */
	public Long getCreationTime();

	/**
	 * 获得session中维护的所有对象名称
	 * @return Enumeration<String>
	 */
	public Enumeration<String> getNames();

	/**
	 * 获得sessionId
	 * @return String
	 */
	public String getId();

	/**
	 * 获得最后访问时间
	 * @return long
	 */
	public long getLastAccessedTime();

	/**
	 * 获得最大存活时间，单位秒
	 * @return int
	 */
	public int getMaxInactiveInterval();

	/**
	 * 清空session中的变量
	 */
	public void invalidate();

	/**
	 * 是否是新创建的session
	 * @return boolean
	 */
	public boolean isNew();

	/**
	 * 移除session中指定的变量
	 * @param name - 名称
	 */
	public void remove(String name);

	/**
	 * 设置session
	 * @param name - 名称
	 * @param value - 值
	 */
	public void set(String name, Object value);

	/**
	 * 设置session最大存活时间
	 * @param value - 秒
	 */
	public void setMaxInactiveInterval(int value);

	/**
	 * 刷新session
	 */
	public void flush();

}
