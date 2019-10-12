package cg.zz.wf.mvc.invoke.converter;

public class ByteConverter implements Converter<Byte> {

	@Override
	public Byte convert(String str) {
		return Byte.valueOf(str);

	}

}
