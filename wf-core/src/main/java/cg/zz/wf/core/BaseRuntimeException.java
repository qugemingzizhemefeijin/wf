package cg.zz.wf.core;

/**
 * 
 * 基础的运行时异常类
 * 
 * @author chengang
 *
 */
public abstract class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 5675067947807956158L;

	public BaseRuntimeException(String message) {
		super(message);
	}

	public BaseRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getMessage() {
		return ExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while ((cause != null) && (cause != rootCause)) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause == null ? this : rootCause;
	}

	public boolean contains(Class<?> exceptionClass) {
		if (exceptionClass == null) {
			return false;
		}
		if (exceptionClass.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if ((cause instanceof BaseRuntimeException)) {
			return ((BaseRuntimeException) cause).contains(exceptionClass);
		}
		while (cause != null) {
			if (exceptionClass.isInstance(cause)) {
				return true;
			}
			if (cause.getCause() == cause) {
				break;
			}
			cause = cause.getCause();
		}
		return false;
	}

}
