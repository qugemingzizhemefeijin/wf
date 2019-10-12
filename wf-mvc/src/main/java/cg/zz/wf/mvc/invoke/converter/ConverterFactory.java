package cg.zz.wf.mvc.invoke.converter;

import java.util.HashMap;
import java.util.Map;

import cg.zz.wf.core.log.Log;
import cg.zz.wf.core.log.LogFactory;

/**
 * 
 * 方法参数转换工厂
 * 
 * @author chengang
 *
 */
public class ConverterFactory {

	private static final Log log = LogFactory.getLog(ConverterFactory.class);

	private Map<Class<?>, Converter<?>> map = new HashMap<>();

	public ConverterFactory() {
		loadInternal();
	}

	void loadInternal() {
		Converter<?> c = null;

		c = new StringConverter();
		//this.map.put(String.class, String::valueOf);
		this.map.put(String.class, c);

		c = new BooleanConverter();
		this.map.put(Boolean.TYPE, c);
		this.map.put(Boolean.class, c);

		c = new CharacterConverter();
		this.map.put(Character.TYPE, c);
		this.map.put(Character.class, c);

		c = new ByteConverter();
		this.map.put(Byte.TYPE, c);
		this.map.put(Byte.class, c);

		c = new ShortConverter();
		this.map.put(Short.TYPE, c);
		this.map.put(Short.class, c);

		c = new IntegerConverter();
		this.map.put(Integer.TYPE, c);
		this.map.put(Integer.class, c);

		c = new LongConverter();
		this.map.put(Long.TYPE, c);
		this.map.put(Long.class, c);

		c = new FloatConverter();
		this.map.put(Float.TYPE, c);
		this.map.put(Float.class, c);

		c = new DoubleConverter();
		this.map.put(Double.TYPE, c);
		this.map.put(Double.class, c);
	}

	/**
	 * 扩展转换类
	 * @param typeClass - 要转换的类型
	 * @param converterClass - 转换的类路径
	 */
	public void loadExternalConverter(String typeClass, String converterClass) {
		try {
			loadExternalConverter(Class.forName(typeClass), (Converter<?>) Class.forName(converterClass).newInstance());
		} catch (Exception e) {
			log.warn("Cannot load converter '" + converterClass + "' for type '" + typeClass + "'.", e);
		}
	}

	/**
	 * 扩展转换类
	 * @param clazz - 要转换的类型
	 * @param converter - 转换器
	 */
	public void loadExternalConverter(Class<?> clazz, Converter<?> converter) {
		if (clazz == null) {
			throw new NullPointerException("Class is null.");
		}
		if (converter == null) {
			throw new NullPointerException("Converter is null.");
		}
		if (this.map.containsKey(clazz)) {
			log.warn("Cannot replace the exist converter for type '" + clazz.getName() + "'.");
			return;
		}
		this.map.put(clazz, converter);
	}

	/**
	 * 判断类别是否能够被解析
	 * @param clazz - Class<?>
	 * @return boolean
	 */
	public boolean canConvert(Class<?> clazz) {
		return (clazz.equals(String.class)) || (this.map.containsKey(clazz));
	}

	/**
	 * 将字符串转换成对应的对象
	 * @param clazz - Class<?>
	 * @param s - String
	 * @return Object
	 */
	public Object convert(Class<?> clazz, String s) {
		Converter<?> c = (Converter<?>) this.map.get(clazz);
		return c.convert(s);
	}

}
