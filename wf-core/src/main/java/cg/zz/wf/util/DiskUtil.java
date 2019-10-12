package cg.zz.wf.util;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public final class DiskUtil {

	public static String home() {
		return System.getProperty("user.home");
	}

	public static String home(String path) {
		return home() + path;
	}

	public static String absolute(String path) {
		return absolute(path, FileUtil.class.getClassLoader(), Charset.defaultCharset().name());
	}

	public static String absolute(String path, ClassLoader klassLoader, String enc) {
		path = normalize(path, enc);
		if (StringUtil.isEmpty(path)) {
			return null;
		}
		File f = new File(path);
		if (!f.exists()) {
			URL url = klassLoader.getResource(path);
			if (url == null) {
				url = Thread.currentThread().getContextClassLoader().getResource(path);
			}
			if (url == null) {
				url = ClassLoader.getSystemResource(path);
			}
			if (url != null) {
				return normalize(url.getPath(), "UTF-8");
			}
			return null;
		}
		return path;
	}

	public static String normalize(String path) {
		return normalize(path, Charset.defaultCharset().name());
	}

	public static String normalize(String path, String enc) {
		if (StringUtil.isEmpty(path)) {
			return null;
		}
		if (path.charAt(0) == '~') {
			path = home() + path.substring(1);
		}
		try {
			return URLDecoder.decode(path, enc);
		} catch (Exception e) {
		}
		return null;
	}

	private DiskUtil() {

	}

}
