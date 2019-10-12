package cg.zz.wf.mvc.bind;

public class CheckedError {

	private ErrorType errorType;
	private String targetName;
	private String message;

	public CheckedError(ErrorType errorType, String targetName, String message) {
		this.errorType = errorType;
		this.targetName = targetName;
		this.message = message;
	}

	public String getTargetName() {
		return this.targetName;
	}

	public String getMessage() {
		return this.message;
	}

	public ErrorType getErrorType() {
		return this.errorType;
	}

	public String toString() {
		return "ValidateError [targetName=" + this.targetName + ", message=" + this.message + "]";
	}

	public static enum ErrorType {
		BIND, VALIDATE;
	}

}
