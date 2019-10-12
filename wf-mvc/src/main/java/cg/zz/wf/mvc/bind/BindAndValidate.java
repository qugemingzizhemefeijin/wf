package cg.zz.wf.mvc.bind;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;

import cg.zz.wf.mvc.BeatContext;

/**
 * 
 * 绑定对象变量以及验证请求参数的合法性
 * 
 * @author chengang
 *
 */
public final class BindAndValidate {

	private static BindAndValidate singleton = null;
	private static ApplicationContext context = null;

	@Autowired
	private Validator validator;

	/**
	 * 传入ApplicationContext
	 * @param context2 - ApplicationContext
	 */
	public static void setApplicationContext(ApplicationContext context2) {
		context = context2;
	}

	/**
	 * BindAndValidate单例对象的获取
	 * @return BindAndValidate
	 */
	public static BindAndValidate Singleton() {
		if (context == null) {
			System.out.println("context is null.");
		}
		if (singleton == null) {
			//???上面contexnt==null为啥不直接返回或者抛出异常，在这里调用一次肯定也会走空指针异常啊，感觉这样子写不太好。。
			singleton = (BindAndValidate) context.getBean("bindAndValidate");
		}
		return singleton;
	}

	/**
	 * 请求参数的绑定
	 * @param target - 绑定对象的实体
	 * @param beat - BeatContext
	 * @return ObjectBindResult
	 */
	public ObjectBindResult bind(Object target, BeatContext beat) {
		ServletRequestDataBinder binder = new ServletRequestDataBinder(target);
		binder.bind(beat.getRequest());
		BindingResult r = binder.getBindingResult();
		return new ObjectBindResult(r);
	}

	/**
	 * 请求参数的绑定
	 * @param targetType - 绑定对象的类型
	 * @param beat - BeatContext
	 * @return ObjectBindResult
	 * @throws Exception
	 */
	public ObjectBindResult bind(Class<?> targetType, BeatContext beat) throws Exception {
		Object target = BeanUtils.instantiateClass(targetType);
		return bind(target, beat);
	}

	/**
	 * 通过hibernate validate验证对象的合法性
	 * @param target - 要验证的对象
	 * @return ObjectBindResult
	 */
	public <T> ObjectBindResult validate(T target) {
		Set<ConstraintViolation<T>> constraintViolations = this.validator.validate(target, new Class[0]);
		List<CheckedError> errors = new ArrayList<>();
		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			CheckedError error = new CheckedError(CheckedError.ErrorType.VALIDATE,constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
			errors.add(error);
		}
		return new ObjectBindResult(target, errors);
	}
	
	private BindAndValidate() {
		
	}

}
