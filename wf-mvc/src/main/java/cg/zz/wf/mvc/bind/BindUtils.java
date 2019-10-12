package cg.zz.wf.mvc.bind;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import cg.zz.wf.mvc.BeatContext;
import cg.zz.wf.mvc.bind.CheckedError.ErrorType;

/**
 * 
 * 这个类跟BindAndValidate类有啥区别？？？难道仅仅一个是spring维护这点区别吗？
 * 
 * @author chengang
 *
 */
public class BindUtils {

	/**
	 * 将请求数据绑定到指定的对象上
	 * @param target - 要绑定数据的对象
	 * @param beat - BeatContext
	 * @return ObjectBindResult
	 */
	public static ObjectBindResult bind(Object target, BeatContext beat) {
		ServletRequestDataBinder binder = new ServletRequestDataBinder(target);
		binder.bind(beat.getRequest());
		return new ObjectBindResult(binder.getBindingResult());
	}

	/**
	 * 将请求的数据绑定到指定的对象类型上
	 * @param targetType - 要绑定的实体类型
	 * @param beat - BeatContext
	 * @return ObjectBindResult
	 * @throws Exception
	 */
	public static ObjectBindResult bind(Class<?> targetType, BeatContext beat) throws Exception {
		return bind(BeanUtils.instantiateClass(targetType), beat);
	}

	/**
	 * 通过hibernate validate验证对象的合法性
	 * @param target - 要验证的对象
	 * @return ObjectBindResult
	 */
	public static <T> ObjectBindResult validate(T target) {
		Set<ConstraintViolation<T>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(target, new Class[0]);
		List<CheckedError> errors = new ArrayList<>();
		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			errors.add(new CheckedError(ErrorType.VALIDATE, constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
		}
		return new ObjectBindResult(target, errors);
	}

	private BindUtils() {

	}

}
