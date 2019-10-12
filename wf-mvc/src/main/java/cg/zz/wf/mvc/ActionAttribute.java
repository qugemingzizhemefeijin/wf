package cg.zz.wf.mvc;

import java.lang.reflect.Method;

/**
 * 
 * 请求方法的描述信息接口
 * 
 * @author chengang
 *
 */
public interface ActionAttribute {
	
	/**
	 * 返回请求的Java方法
	 * @return Method
	 */
	public Method getActionMethod();

}
