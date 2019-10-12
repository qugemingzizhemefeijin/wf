package cg.zz.wf.mvc.invoke.converter;

public class FloatConverter implements Converter<Float> {

	@Override
	public Float convert(String str) {
		return Float.valueOf(str);

	}

}
