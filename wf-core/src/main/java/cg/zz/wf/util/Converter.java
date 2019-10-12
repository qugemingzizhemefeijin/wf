package cg.zz.wf.util;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class Converter {

	private static Map<String, String> sqlMatches = new HashMap<>();
	private static Map<Character, String> htmlEncodeMatches = new HashMap<>();
	private static Map<String, Character> htmlDecodeMatches = new HashMap<>();
	private static final Properties sqlInjectProperties = new Properties();
	private static final Properties htmlEncodeProperties = new Properties();

	public static String convert(String source) {
		//此处可以优化为sqlMatches为一个Set，不然每次都需要转换也不划算呀。
		for (String key : sqlMatches.keySet()) {
			source = source.replaceAll("[" + (String) sqlMatches.get(key) + "]", key);
		}
		source = encodeHtml(source);

		return source;
	}
	
	/**
	 * 初始化SQL的防注入匹配规则
	 * @param configFile - 文件路径
	 */
	public static void initSqlInject(String configFile) {
		File f = new File(configFile);
		try(FileReader fr = new FileReader(f)) {
			sqlInjectProperties.load(fr);
			
			Enumeration<?> e = sqlInjectProperties.propertyNames();
			while(e.hasMoreElements()) {
				String key = (String)e.nextElement();
				String value = sqlMatches.get(key);
				if(value != null) {
					sqlMatches.put(key, value+"|"+sqlInjectProperties.getProperty(key , ""));
				} else {
					sqlMatches.put(key, sqlInjectProperties.getProperty(key , ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化Html的替换规则
	 * @param configFile - 文件路径 
	 */
	public static void initHtmlEncode(String configFile) {
		File f = new File(configFile);
		try(FileReader fr = new FileReader(f)) {
			htmlEncodeProperties.load(fr);
			
			Enumeration<?> e = htmlEncodeProperties.propertyNames();
			while(e.hasMoreElements()) {
				String key = ((String)e.nextElement()).trim();
				char c = key.charAt(0);
				
				String value = htmlEncodeProperties.getProperty(key, "");
				htmlEncodeMatches.put(c, value);
				htmlDecodeMatches.put(value, c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String encodeHtml(String str) {
		if (str == null) {
			return null;
		}
		StringWriter writer = new StringWriter((int) (str.length() * 1.5D));
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (htmlEncodeMatches.get(Character.valueOf(c)) == null) {
				writer.write(c);
			} else {
				writer.write((String) htmlEncodeMatches.get(Character.valueOf(c)));
			}
		}
		return writer.toString();
	}
	
	public static String decodeHtml(String str) throws Exception {
		throw new Exception("no impl");
	}

	private Converter() {

	}

}
