package cg.zz.wf.mvc.invoke.converter;

public class IntegerConverter implements Converter<Integer> {

	@Override
	public Integer convert(String str) {
		return Integer.valueOf(str);
	}

}
