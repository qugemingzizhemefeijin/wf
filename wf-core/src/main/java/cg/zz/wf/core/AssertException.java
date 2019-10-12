package cg.zz.wf.core;

/**
 * 
 * 断言异常类
 * 
 * @author chengang
 *
 */
public class AssertException extends BaseRuntimeException {

	private static final long serialVersionUID = -6443659510835398510L;

	public AssertException(String message, Throwable cause) {
		super(message, cause);
	}

	public AssertException(String message) {
		super(message);
	}

	public AssertException() {
		super("Assert Exception ->");
	}

}
