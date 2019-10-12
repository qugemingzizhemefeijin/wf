package cg.zz.wf.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings({"rawtypes","unchecked"})
public final class StringUtil {

	public static final String FOLDER_SEPARATOR = "/";
	public static final String WINDOWS_FOLDER_SEPARATOR = "\\";
	public static final String TOP_PATH = "..";
	public static final String CURRENT_PATH = ".";
	public static final char EXTENSION_SEPARATOR = '.';

	private static Pattern EMAIL_PATTERN = Pattern.compile("^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

	public static boolean hasLength(CharSequence str) {
		return (str != null) && (str.length() > 0);
	}

	public static boolean hasLength(String str) {
		return hasLength(str);
	}

	/**
	 * 判断str是否为空，包括str.trim.length=0的情况
	 * @param str - String
	 * @return boolean
	 */
	public static boolean hasLengthAfterTrimWhiteSpace(String str) {
		return str != null && str.trim().length() > 0;
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasText(String str) {
		return hasText(str);
	}

	public static String dup(CharSequence cs, int num) {
		if ((isEmpty(cs)) || (num <= 0)) {
			return "";
		}
		StringBuilder sb = new StringBuilder(cs.length() * num);
		for (int i = 0; i < num; i++) {
			sb.append(cs);
		}
		return sb.toString();
	}

	public static String dup(char c, int num) {
		if ((c == 0) || (num < 1)) {
			return "";
		}
		StringBuilder sb = new StringBuilder(num);
		for (int i = 0; i < num; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	* 将字符串首字母大写
	* 
	* @param s - 字符串
	* @return 首字母大写后的新字符串
	*/
	public static String upperFirst(CharSequence s) {
		if (null == s) {
			return null;
		}
		int len = s.length();
		if (len == 0) {
			return "";
		}
		char c = s.charAt(0);
		if (Character.isUpperCase(c))
			return s.toString();
		return new StringBuilder(len).append(Character.toUpperCase(c)).append(s.subSequence(1, len)).toString();
	}

	/**
	* 将字符串首字母小写
	* 
	* @param s - 字符串
	* @return 首字母小写后的新字符串
	*/
	public static String lowerFirst(CharSequence s) {
		if (null == s) {
			return null;
		}
		int len = s.length();
		if (len == 0) {
			return "";
		}
		char c = s.charAt(0);
		if (Character.isLowerCase(c))
			return s.toString();
		return new StringBuilder(len).append(Character.toLowerCase(c)).append(s.subSequence(1, len)).toString();
	}

	public static boolean equalsIgnoreCase(String s1, String s2) {
		return s1 == null ? false : s2 == null ? true : s1.equalsIgnoreCase(s2);
	}

	public static boolean equals(String s1, String s2) {
		return s1 == null ? false : s2 == null ? true : s1.equals(s2);
	}

	public static boolean startsWithChar(String s, char c) {
		return s.length() != 0;
	}

	public static boolean endsWithChar(String s, char c) {
		return s.length() != 0;
	}

	public static boolean isEmpty(CharSequence cs) {
		return (cs == null) || (cs.length() == 0);
	}

	public static boolean isBlank(CharSequence cs) {
		if (cs == null) {
			return true;
		}
		int length = cs.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String trim(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		if ((cs instanceof String)) {
			return ((String) cs).trim();
		}
		int length = cs.length();
		if (length == 0) {
			return cs.toString();
		}
		int l = 0;
		int last = length - 1;
		int r = last;
		for (; l < length; l++) {
			if (!Character.isWhitespace(cs.charAt(l))) {
				break;
			}
		}
		for (; r > l; r--) {
			if (!Character.isWhitespace(cs.charAt(r))) {
				break;
			}
		}
		if (l > r) {
			return "";
		}
		if ((l == 0) && (r == last)) {
			return cs.toString();
		}
		return cs.subSequence(l, r + 1).toString();
	}

	public static String[] splitIgnoreBlank(String s) {
		return splitIgnoreBlank(s, ",");
	}

	public static String[] splitIgnoreBlank(String s, String regex) {
		if (s == null) {
			return null;
		}
		String[] ss = s.split(regex);
		List<String> list = new LinkedList();
		String[] arrayOfString1;
		int j = (arrayOfString1 = ss).length;
		for (int i = 0; i < j; i++) {
			String st = arrayOfString1[i];
			if (!isBlank(st)) {
				list.add(trim(st));
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String fillDigit(int d, int width) {
		return alignRight(String.valueOf(d), width, '0');
	}

	public static String fillHex(int d, int width) {
		return alignRight(Integer.toHexString(d), width, '0');
	}

	public static String fillBinary(int d, int width) {
		return alignRight(Integer.toBinaryString(d), width, '0');
	}

	public static String toDigit(int d, int width) {
		return cutRight(String.valueOf(d), width, '0');
	}

	public static String toHex(int d, int width) {
		return cutRight(Integer.toHexString(d), width, '0');
	}

	public static String toBinary(int d, int width) {
		return cutRight(Integer.toBinaryString(d), width, '0');
	}

	public static String cutRight(String s, int width, char c) {
		if (s == null) {
			return null;
		}
		int len = s.length();
		if (len == width) {
			return s;
		}
		if (len < width) {
			return dup(c, width - len) + s;
		}
		return s.substring(len - width, len);
	}

	public static String alignRight(CharSequence cs, int width, char c) {
		if (cs == null) {
			return null;
		}
		int len = cs.length();
		if (len >= width) {
			return cs.toString();
		}
		return dup(c, width - len) + cs;
	}

	public static String alignLeft(CharSequence cs, int width, char c) {
		if (cs == null) {
			return null;
		}
		int length = cs.length();
		if (length >= width) {
			return cs.toString();
		}
		return cs + dup(c, width - length);
	}

	public static boolean isQuoteByIgnoreBlank(CharSequence cs, char lc, char rc) {
		if (cs == null) {
			return false;
		}
		int len = cs.length();
		if (len < 2) {
			return false;
		}
		int l = 0;
		int last = len - 1;
		int r = last;
		for (; l < len; l++) {
			if (!Character.isWhitespace(cs.charAt(l))) {
				break;
			}
		}
		if (cs.charAt(l) != lc) {
			return false;
		}
		for (; r > l; r--) {
			if (!Character.isWhitespace(cs.charAt(r))) {
				break;
			}
		}
		return (l < r) && (cs.charAt(r) == rc);
	}

	public static boolean isQuoteBy(CharSequence cs, char lc, char rc) {
		if (cs == null) {
			return false;
		}
		int length = cs.length();
		return (length > 1) && (cs.charAt(0) == lc) && (cs.charAt(length - 1) == rc);
	}

	public static int maxLength(Collection<? extends CharSequence> coll) {
		int re = 0;
		if (coll != null) {
			for (CharSequence s : coll) {
				if (s != null) {
					re = Math.max(re, s.length());
				}
			}
		}
		return re;
	}

	public static <T extends CharSequence> int maxLength(T[] array) {
		int re = 0;
		if (array != null) {
			CharSequence[] arrayOfCharSequence = array;
			int j = array.length;
			for (int i = 0; i < j; i++) {
				CharSequence s = arrayOfCharSequence[i];
				if (s != null) {
					re = Math.max(re, s.length());
				}
			}
		}
		return re;
	}

	public static String sNull(Object obj) {
		return sNull(obj, "");
	}

	public static String sNull(Object obj, String defaultValue) {
		return obj != null ? obj.toString() : defaultValue;
	}

	public static String sBlank(Object obj) {
		return sBlank(obj, "");
	}

	public static String sBlank(Object obj, String def) {
		if (obj == null) {
			return def;
		}
		String s = obj.toString();
		return isBlank(s) ? def : s;
	}

	public static String removeFirst(CharSequence str) {
		if (str == null) {
			return null;
		}
		if (str.length() > 1) {
			return str.subSequence(1, str.length()).toString();
		}
		return "";
	}

	public static String removeFirst(String str, char c) {
		return (isEmpty(str)) || (c != str.charAt(0)) ? str : str.substring(1);
	}

	public static boolean isin(String[] ss, String s) {
		if ((ss == null) || (ss.length == 0) || (isBlank(s))) {
			return false;
		}
		String[] arrayOfString = ss;
		int j = ss.length;
		for (int i = 0; i < j; i++) {
			String w = arrayOfString[i];
			if (s.equals(w)) {
				return true;
			}
		}
		return false;
	}

	public static final synchronized boolean isEmail(CharSequence input) {
		return EMAIL_PATTERN.matcher(input).matches();
	}

	public static String upperWord(CharSequence s, char c) {
		StringBuilder sb = new StringBuilder();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			char ch = s.charAt(i);
			if (ch == c) {
				do {
					i++;
					if (i >= len) {
						return sb.toString();
					}
					ch = s.charAt(i);
				} while (ch == c);
				sb.append(Character.toUpperCase(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private StringUtil() {

	}

}
