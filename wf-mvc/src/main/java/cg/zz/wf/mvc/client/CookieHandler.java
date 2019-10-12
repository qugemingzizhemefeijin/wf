package cg.zz.wf.mvc.client;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.util.StringUtil;

/**
 * 
 * Cookie处理器
 * 
 * @author chengang
 *
 */
public class CookieHandler {

	HttpServletResponse response;
	HttpServletRequest request;

	public CookieHandler(BeatContext beat) {
		this.response = beat.getResponse();
		this.request = beat.getRequest();
	}

	/**
	 * 添加Cookie到/目录下
	 * @param name - 名称
	 * @param value - 值
	 */
	public void add(String name, String value) {
		Cookie cookie = new Cookie(name, value);

		cookie.setPath("/");

		this.response.addCookie(cookie);
	}

	/**
	 * 添加Cookie到/目录下
	 * @param name - 名称
	 * @param value - 值
	 * @param time - 过期时间
	 */
	public void add(String name, String value, int time) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(time);
		cookie.setPath("/");
		add(cookie);
	}

	/**
	 * 添加Cookie
	 * @param cookie
	 */
	public void add(Cookie cookie) {
		this.response.addCookie(cookie);
	}

	/**
	 * 根据Cookie的名称获得值
	 * @param name - 名称
	 * @return String
	 */
	public String get(String name) {
		Cookie cookie = getCookie(name);
		return cookie == null ? null : cookie.getValue();
	}

	/**
	 * 根据Cookie的名称查询指定的Cookie对象
	 * @param name - 名称
	 * @return Cookie
	 */
	private Cookie getCookie(String name) {
		Cookie[] cookies = this.request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return null;
		}
		
		for(Cookie cookie : cookies) {
			if (name.equalsIgnoreCase(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	/**
	 * 查询出Cookie对象，如果对象为空则写入Cookie值，否则替换Cookie值
	 * @param name - 名称
	 * @param value - 值
	 */
	public void set(String name, String value) {
		Cookie cookie = getCookie(name);
		if (cookie == null) {
			add(name, value);
			return;
		}
		cookie.setValue(value);
	}

	/**
	 * 查询出Cookie对象，如果对象为空则写入Cookie值，否则替换Cookie值
	 * @param name - 名称
	 * @param value - 值
	 * @param time - 过期时间
	 */
	public void set(String name, String value, int time) {
		Cookie cookie = getCookie(name);
		if (cookie == null) {
			add(name, value, time);
			return;
		}
		cookie.setValue(value);
		cookie.setMaxAge(time);
	}

	/**
	 * 删除Cookie
	 * @param name - 名称
	 */
	public void delete(String name) {
		Cookie cookie = getCookie(name);
		if (cookie == null) {
			return;
		}
		cookie.setMaxAge(0);
		this.response.addCookie(cookie);
	}

	/**
	 * 添加Cookie，如果encoder是URL，则将Cookie的值使用URLEncoder编码后再写入Cookie，否则使用Base64编码后写入
	 * @param cookie - Cookie
	 * @param encoder - 编码要求
	 * @throws UnsupportedEncodingException
	 */
	public void add(Cookie cookie, String encoder) throws UnsupportedEncodingException {
		if ("URL".equals(encoder)) {
			cookie.setValue(URLEncoder.encode(cookie.getValue(), Charsets.UTF_8.name()));
		} else {
			cookie.setValue(new String(Base64.encodeBase64(cookie.getValue().getBytes())));
		}
		this.response.addCookie(cookie);
	}

	/**
	 * 根据编码要求获取Cookie的值
	 * @param cookieName - 名称
	 * @param decoder - 解码要求
	 * @return String
	 */
	public String getCookie(String cookieName, String decoder) {
		String cookieValue = "";
		String cookieHeader = "";
		Enumeration<String> cookieHeaderE = this.request.getHeaders("cookie");
		//查找到头信息中的Cookie字段，有可能 存在多个，所以需要循环来查找哪一个字符串
		while (cookieHeaderE.hasMoreElements()) {
			cookieHeader = cookieHeaderE.nextElement();
			if (!StringUtil.isEmpty(cookieHeader) && cookieHeader.contains(cookieName)) {
				break;
			}
		}
		if (!StringUtil.isEmpty(cookieHeader)) {
			for(String cookieStr : cookieHeader.split(";")) {
				if (cookieStr.startsWith(cookieName + "=")) {
					cookieValue = cookieStr.replaceFirst(cookieName + "=", "");
					try {
						cookieValue = cookieValue.replaceAll("\"", "");
						if ("URL".equals(decoder)) {
							cookieValue = URLDecoder.decode(cookieValue, "UTF-8");
						} else {
							cookieValue = new String(Base64.decodeBase64(cookieValue));
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return cookieValue.replaceAll("\"", "");
				}
			}
		}
		return "";
	}

}
