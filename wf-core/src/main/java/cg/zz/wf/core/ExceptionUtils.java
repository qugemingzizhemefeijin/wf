package cg.zz.wf.core;

import java.lang.reflect.InvocationTargetException;

import cg.zz.wf.util.AssertUtil;

@SuppressWarnings({"rawtypes","unchecked"})
public final class ExceptionUtils {

	public static String buildMessage(String message, Throwable cause) {
		if (cause != null) {
			StringBuilder sb = new StringBuilder();
			if (message != null) {
				sb.append(message).append("; ");
			}
			sb.append("base exception is ").append(cause);
			return sb.toString();
		}
		return message;
	}

	public static boolean isCheckedException(Throwable ex) {
		return (!(ex instanceof RuntimeException)) && (!(ex instanceof Error));
	}

	public static boolean isCompatibleWithThrowsClause(Throwable ex, Class[] declaredExceptions) {
		if (!isCheckedException(ex)) {
			return true;
		}
		if (declaredExceptions != null) {
			int i = 0;
			while (i < declaredExceptions.length) {
				if (declaredExceptions[i].isAssignableFrom(ex.getClass())) {
					return true;
				}
				i++;
			}
		}
		return false;
	}

	public static BaseRuntimeException makeThrow(String message) {
		return new BaseRuntimeException(message) {

			private static final long serialVersionUID = -3944169686217152547L;
			
		};
	}

	public static BaseRuntimeException makeThrow(String message, Throwable cause) {
		return new BaseRuntimeException(message, cause) {

			private static final long serialVersionUID = 5591695101719973624L;
			
		};
	}

	public static BaseRuntimeException makeThrow(String format, Object... message) {
		return makeThrow(String.format(format, message));
	}

	public static Throwable getTargetException(Exception exception) {
		AssertUtil.notNull(exception);
		if ((exception instanceof InvocationTargetException)) {
			Throwable target = ((InvocationTargetException) exception).getTargetException();
			if (target != null) {
				return target;
			}
		}
		return exception;
	}

	private ExceptionUtils() {

	}

}
