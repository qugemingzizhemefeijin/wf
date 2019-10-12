package cg.zz.wf.mvc.invoke.converter;

public class BooleanConverter implements Converter<Boolean> {

	@Override
	public Boolean convert(String str) {
		return Boolean.valueOf(str);

	}

}
