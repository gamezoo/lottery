package me.zohar.lottery.common.valid;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.ParamValidException;

/**
 * Spring AOP实现参数校验
 * 
 * @author zohar
 * @date 2019年1月17日
 *
 */
@Aspect
@Component
public class ParamValidAspect {

	@Autowired
	private Validator validator;

	@Pointcut("@annotation(me.zohar.lottery.common.valid.ParamValid)")
	public void paramValidAspect() {
	}

	@Before("paramValidAspect()")
	public void before(JoinPoint joinPoint) {
		// 获取入参
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg == null) {
				throw new ParamValidException(BizError.参数异常.getCode(), "入参为空");
			}
			Set<ConstraintViolation<Object>> violations = validator.validate(arg);
			Iterator<ConstraintViolation<Object>> iterator = violations.iterator();
			// 参数校验不通过,直接抛出异常
			if (iterator.hasNext()) {
				ConstraintViolation<Object> violation = iterator.next();
				throw new ParamValidException(BizError.参数异常.getCode(),
						violation.getPropertyPath() + ":" + violation.getMessage());
			}
		}

	}

}
