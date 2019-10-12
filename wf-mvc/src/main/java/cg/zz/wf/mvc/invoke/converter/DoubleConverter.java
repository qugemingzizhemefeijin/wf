package cg.zz.wf.mvc.invoke.converter;

public class DoubleConverter implements Converter<Double> {

	@Override
	public Double convert(String str) {
		return Double.valueOf(str);

	}

}
