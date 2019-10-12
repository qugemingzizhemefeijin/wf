package cg.zz.wf.core;

public class ResourceNotFoundException extends BaseRuntimeException {

	private static final long serialVersionUID = 2637422234775841717L;

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super("Resource not found ->", cause);
	}

}
