package cg.zz.wf.util;

import java.util.Collection;
import java.util.Map;

import cg.zz.wf.core.AssertException;

@SuppressWarnings({"rawtypes","unchecked"})
public final class AssertUtil {

	public static void assertEquals(String message, Object expected, Object actual) {
		if ((expected == null) && (actual == null)) {
			return;
		}
		if ((expected != null) && (expected.equals(actual))) {
			return;
		}
		failNotEquals(message, expected, actual);
	}

	public static void assertEquals(Object expected, Object actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, String expected, String actual) {
		if ((expected == null) && (actual == null)) {
			return;
		}
		if ((expected != null) && (expected.equals(actual))) {
			return;
		}
		throw new AssertException(format(message, expected, actual));
	}

	public static void assertEquals(String expected, String actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, double expected, double actual, double delta) {
		if (Double.compare(expected, actual) == 0) {
			return;
		}
		if (Math.abs(expected - actual) > delta) {
			failNotEquals(message, new Double(expected), new Double(actual));
		}
	}

	public static void assertEquals(double expected, double actual, double delta) {
		assertEquals(null, expected, actual, delta);
	}

	public static void assertEquals(String message, float expected, float actual, float delta) {
		if (Float.compare(expected, actual) == 0) {
			return;
		}
		if (Math.abs(expected - actual) > delta) {
			failNotEquals(message, new Float(expected), new Float(actual));
		}
	}

	public static void assertEquals(float expected, float actual, float delta) {
		assertEquals(null, expected, actual, delta);
	}

	public static void assertEquals(String message, long expected, long actual) {
		assertEquals(message, new Long(expected), new Long(actual));
	}

	public static void assertEquals(long expected, long actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, boolean expected, boolean actual) {
		assertEquals(message, Boolean.valueOf(expected), Boolean.valueOf(actual));
	}

	public static void assertEquals(boolean expected, boolean actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, byte expected, byte actual) {
		assertEquals(message, new Byte(expected), new Byte(actual));
	}

	public static void assertEquals(byte expected, byte actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, char expected, char actual) {
		assertEquals(message, new Character(expected), new Character(actual));
	}

	public static void assertEquals(char expected, char actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, short expected, short actual) {
		assertEquals(message, new Short(expected), new Short(actual));
	}

	public static void assertEquals(short expected, short actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, int expected, int actual) {
		assertEquals(message, new Integer(expected), new Integer(actual));
	}

	public static void assertEquals(int expected, int actual) {
		assertEquals(null, expected, actual);
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new AssertException(message);
		}
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isFalse(boolean expression) {
		isFalse(expression, "[Assertion failed] - this expression must be false");
	}

	public static void isFalse(boolean expression, String message) {
		if (expression) {
			throw new AssertException(message);
		}
	}

	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new AssertException(message);
		}
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new AssertException(message);
		}
	}

	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static void hasLength(String text, String message) {
		if (!StringUtil.hasLength(text)) {
			throw new AssertException(message);
		}
	}

	public static void hasLength(String text) {
		hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void hasText(String text, String message) {
		if (!StringUtil.hasText(text)) {
			throw new AssertException(message);
		}
	}

	public static void hasText(String text) {
		hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	public static void doesNotContain(String textToSearch, String substring, String message) {
		if ((StringUtil.hasLength(textToSearch)) && (StringUtil.hasLength(substring))
				&& (textToSearch.indexOf(substring) != -1)) {
			throw new AssertException(message);
		}
	}

	public static void doesNotContain(String textToSearch, String substring) {
		doesNotContain(textToSearch, substring,
				"[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
	}

	public static void notEmpty(Object[] array, String message) {
		if ((array == null) || (array.length == 0)) {
			throw new AssertException(message);
		}
	}

	public static void notEmpty(Object[] array) {
		notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					throw new AssertException(message);
				}
			}
		}
	}

	public static void noNullElements(Object[] array) {
		noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	public static void notEmpty(Collection collection, String message) {
		if (CollectionUtil.isEmpty(collection)) {
			throw new AssertException(message);
		}
	}

	public static void notEmpty(Collection collection) {
		notEmpty(collection,
				"[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	public static void notEmpty(Map map, String message) {
		if (CollectionUtil.isEmpty(map)) {
			throw new AssertException(message);
		}
	}

	public static void notEmpty(Map map) {
		notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	public static void isInstanceOf(Class clazz, Object obj) {
		isInstanceOf(clazz, obj, "");
	}

	public static void isInstanceOf(Class type, Object obj, String message) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throw new AssertException(message + "Object of class [" + (obj != null ? obj.getClass().getName() : "null")
					+ "] must be an instance of " + type);
		}
	}

	public static void isAssignable(Class superType, Class subType) {
		isAssignable(superType, subType, "");
	}

	public static void isAssignable(Class superType, Class subType, String message) {
		notNull(superType, "Type to check against must not be null");
		if ((subType == null) || (!superType.isAssignableFrom(subType))) {
			throw new AssertException(message + subType + " is not assignable to " + superType);
		}
	}

	public static void fail(String message) {
		throw new AssertException(message);
	}

	public static void fail() {
		fail(null);
	}

	public static void failNotEquals(String message, Object expected, Object actual) {
		fail(format(message, expected, actual));
	}

	public static void state(boolean expression, String message) {
		if (!expression) {
			throw new AssertException(message);
		}
	}

	public static void state(boolean expression) {
		state(expression, "[Assertion failed] - this state invariant must be true");
	}

	public static String format(String message, Object expected, Object actual) {
		String formatted = "";
		if (message != null) {
			formatted = message + " ";
		}
		return formatted + "expected:<" + expected + "> but was:<" + actual + ">";
	}

	private AssertUtil() {

	}

}
