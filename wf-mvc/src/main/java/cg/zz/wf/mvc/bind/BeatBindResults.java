package cg.zz.wf.mvc.bind;

import java.util.ArrayList;
import java.util.List;

import cg.zz.wf.util.CollectionUtil;

/**
 * 
 * 错误信息结果对象
 * 
 * @author chengang
 *
 */
public class BeatBindResults {

	private List<ObjectBindResult> results = new ArrayList<>();

	public ObjectBindResult get(Object target) {
		for (ObjectBindResult result : this.results) {
			if (result.getTarget().equals(target)) {
				return result;
			}
		}
		return null;
	}

	public void add(ObjectBindResult other) {
		for (ObjectBindResult result : this.results) {
			if (result.getTarget().equals(other.getTarget())) {
				result.merge(other);
				return;
			}
		}
		this.results.add(other);
	}

	public List<ObjectBindResult> getErrorBindResults() {
		List<ObjectBindResult> errorResults = new ArrayList<>();
		for (ObjectBindResult result : this.results) {
			if (result.getErrorCount() > 0) {
				errorResults.add(result);
			}
		}
		return errorResults;
	}

	public boolean hasError() {
		return getErrorBindResults().size() > 0;
	}

	/**
	 * 获得所有的错误信息
	 * @return CheckedError[]
	 */
	public CheckedError[] getErrors() {
		List<CheckedError> errors = new ArrayList<>();
		for (ObjectBindResult br : getErrorBindResults()) {
			List<CheckedError> checkErrorList = br.getErrors();
			if(!CollectionUtil.isEmpty(checkErrorList)) {
				for(CheckedError e : checkErrorList) {
					errors.add(e);
				}
			}
		}
		return (CheckedError[]) errors.toArray(new CheckedError[errors.size()]);
	}

}
