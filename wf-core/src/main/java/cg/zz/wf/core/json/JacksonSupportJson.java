package cg.zz.wf.core.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cg.zz.wf.core.log.Log;
import cg.zz.wf.core.log.LogFactory;
import cg.zz.wf.util.StringUtil;

/**
 * 
 * Jackson对象
 * 
 * @author chengang
 *
 */
public class JacksonSupportJson implements Json {
	
	private static final Log LOGGER = LogFactory.getLog(JacksonSupportJson.class);
	
	private ObjectMapper objectMapper;
	
	public JacksonSupportJson(Include inclusion) {
		this.objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(inclusion);
		
		// 去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// 反序列化时，属性不存在的兼容处理
		objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 单引号处理
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	}
	
	public static Json buildNormalBinder() {
		return new JacksonSupportJson(Include.ALWAYS);
	}
	
	public static Json buildNonNullBinder() {
		return new JacksonSupportJson(Include.NON_NULL);
	}
	
	public static Json buildNonDefaultBinder() {
		return new JacksonSupportJson(Include.NON_DEFAULT);
	}

	@Override
	public <T> T fromJson(String s, Class<T> cls) {
		if (!StringUtil.hasLengthAfterTrimWhiteSpace(s)) {
			return null;
		}
		try {
			return (T) this.objectMapper.readValue(s, cls);
		} catch (IOException e) {
			LOGGER.warn("parse json string error:" + s, e);
		}
		return null;
	}

	@Override
	public String toJson(Object o) {
		try {
			return this.objectMapper.writeValueAsString(o);
		} catch (IOException e) {
			LOGGER.warn("write to json string error:" + o, e);
		}
		return null;
	}

	@Override
	public void setDateFormat(String pattern) {
		if (StringUtil.hasLengthAfterTrimWhiteSpace(pattern)) {
			this.objectMapper.setDateFormat(new SimpleDateFormat(pattern));
		}
	}

	@Override
	public Object getMapper() {
		return this.objectMapper;
	}

}
