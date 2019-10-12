package cg.zz.wf.mvc.invoke.converter;

public class LongConverter implements Converter<Long> {

	@Override
	public Long convert(String str) {
		return Long.valueOf(str);

	}

}
