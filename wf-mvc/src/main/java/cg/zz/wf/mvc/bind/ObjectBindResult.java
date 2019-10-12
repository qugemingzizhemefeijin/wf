package cg.zz.wf.mvc.bind;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class ObjectBindResult {

	private Object target;
	private List<CheckedError> errors = new ArrayList<>();

	ObjectBindResult(BindingResult result) {
		List<ObjectError> springErrors = result.getAllErrors();
		for (ObjectError springError : springErrors) {
			CheckedError error = new CheckedError(CheckedError.ErrorType.BIND, springError.getObjectName(), springError.getDefaultMessage());
			this.errors.add(error);
		}
		this.target = result.getTarget();
	}

	public ObjectBindResult(Object target, List<CheckedError> errors) {
		this.target = target;
		this.errors = errors;
	}

	public Object getTarget() {
		return this.target;
	}

	public List<CheckedError> getErrors() {
		return this.errors;
	}

	public int getErrorCount() {
		return this.errors.size();
	}

	public void merge(ObjectBindResult result) {
		this.errors.addAll(result.getErrors());
	}

}
