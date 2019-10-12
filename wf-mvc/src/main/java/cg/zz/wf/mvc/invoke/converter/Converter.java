package cg.zz.wf.mvc.invoke.converter;

/**
 * 
 * 字符串转换器接口
 * 
 * @author chengang
 *
 * @param <T>
 */
@FunctionalInterface
public interface Converter<T> {
	
	/**
	 * 将字符串转换成指定类型的值
	 * @param str - String
	 * @return T
	 */
	public T convert(String str);

}
