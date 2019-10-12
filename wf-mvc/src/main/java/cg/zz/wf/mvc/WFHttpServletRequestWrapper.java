package cg.zz.wf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cg.zz.wf.util.Converter;

/**
 * 
 * HttpServletRequestWrapper的包装类
 * 
 * @author chengang
 *
 */
public class WFHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final BeatContext beat;

	public WFHttpServletRequestWrapper(HttpServletRequest request, BeatContext beat) {
		super(request);
		this.beat = beat;
	}

	/**
	 * 根据请求参数名称获得原始的值
	 * @param name - 名称
	 * @return String
	 */
	public String getOriginalParameter(String name) {
		return super.getParameter(name);
	}

	/**
	 * 根据请求参数名称获得原始的值
	 * @param name - 名称
	 * @return String[]
	 */
	public String[] getOriginalParameterValues(String name) {
		return super.getParameterValues(name);
	}

	/**
	 * 获得请求参数，如果参数需要转换则调用Converter.convert
	 */
	public String getParameter(String name) {
		String source = super.getParameter(name);
		if (source == null || source.length() == 0) {
			return null;
		}

		//是否有不需要转换的参数
		String[] pwv = this.beat.getParamWithoutValidate();
		if (pwv == null) {
			return Converter.convert(source);
		}
		for(String s : pwv) {
			//如果参数不需要转换，则直接返回原始的值
			if (name.equals(s)) {
				return source;
			}
		}
		//匹配不到还是需要进行转换的
		return Converter.convert(source);
	}

	/**
	 * 获得请求的参数数组
	 */
	public String[] getParameterValues(String name) {
		String[] ss = super.getParameterValues(name);
		if (ss == null || ss.length == 0) {
			return null;
		}
		String[] pwvs = this.beat.getParamWithoutValidate();
		
		for (int i = 0; i < ss.length; i++) {
			String s = ss[i];
			if (s != null && s.length() > 0) {
				if (pwvs == null || pwvs.length == 0) {
					ss[i] = Converter.convert(s);
				} else {
					//判断是否需要转换
					boolean needConvert = true;
					for(String pwv : pwvs) {
						if (name.equals(pwv)) {
							needConvert = false;
							break;
						}
					}
					if (needConvert) {
						ss[i] = Converter.convert(s);
					}
				}
			}
		}
		return ss;
	}

}
