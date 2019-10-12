package cg.zz.wf.mvc.invoke.converter;

public class CharacterConverter implements Converter<Character> {

	@Override
	public Character convert(String str) {
		if (str.length() != 0) {
			return Character.valueOf(str.charAt(0));
		}
		throw new IllegalArgumentException("Cannot convert empty string to char.");

	}

}
