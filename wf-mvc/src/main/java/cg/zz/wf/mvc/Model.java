package cg.zz.wf.mvc;

import java.util.Map;

import org.springframework.ui.ModelMap;

/**
 * 
 * MVC Model
 * 
 * @author chengang
 *
 */
public class Model {

	private ModelMap model;

	@Deprecated
	public void addAttribute(Object attributeValue) {
		getModelMap().addAttribute(attributeValue);
	}

	/**
	 * 将值添加到Model中，名字是value类型
	 * @param attributeValue - Object
	 */
	public void add(Object attributeValue) {
		getModelMap().addAttribute(attributeValue);
	}

	@Deprecated
	public void addAttribute(String attributeName, Object attributeValue) {
		getModelMap().addAttribute(attributeName, attributeValue);
	}

	/**
	 * 将指定的值添加到Model中，名字是attributeName
	 * @param attributeName - 名称
	 * @param attributeValue - 值
	 */
	public void add(String attributeName, Object attributeValue) {
		getModelMap().addAttribute(attributeName, attributeValue);
	}

	/**
	 * 根据名称获得值
	 * @param attributeName - String
	 * @return Object
	 */
	public Object getAttributeValue(String attributeName) {
		return getModelMap().get(attributeName);
	}

	@Deprecated
	public Object get(String attributeName) {
		return getModelMap().get(attributeName);
	}

	public Map<String, Object> getModel() {
		return getModelMap();
	}

	@Deprecated
	public void addAllAttributes(Map<String, ?> attributes) {
		getModelMap().addAllAttributes(attributes);
	}

	/**
	 * 将Map添加到Model中
	 * @param attributes - Map<String , ?>
	 */
	public void addAll(Map<String, ?> attributes) {
		getModelMap().addAllAttributes(attributes);
	}

	@Deprecated
	public boolean containsAttribute(String attributeName) {
		return getModelMap().containsAttribute(attributeName);
	}

	/**
	 * 判断Model中是否有指定的名称
	 * @param attributeName - String
	 * @return boolean
	 */
	public boolean contains(String attributeName) {
		return getModelMap().containsAttribute(attributeName);
	}

	/**
	 * 将Map添加到Model中，如果存在相同的名称，则不会被替换
	 * @param attributes - Map<String , ?>
	 */
	public void merge(Map<String, ?> attributes) {
		getModelMap().mergeAttributes(attributes);
	}

	private ModelMap getModelMap() {
		if (this.model == null) {
			this.model = new ModelMap();
		}
		return this.model;
	}

}
