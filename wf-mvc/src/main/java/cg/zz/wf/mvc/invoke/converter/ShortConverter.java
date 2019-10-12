package cg.zz.wf.mvc.invoke.converter;

public class ShortConverter implements Converter<Short> {

	@Override
	public Short convert(String str) {
		return Short.valueOf(str);

	}

}
