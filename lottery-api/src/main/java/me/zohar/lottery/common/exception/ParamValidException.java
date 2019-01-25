package me.zohar.lottery.common.exception;

import lombok.Getter;

/**
 * 参数校验异常
 * 
 * @author zohar
 * @date 2019年1月17日
 *
 */
@Getter
public class ParamValidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	private String msg;

	public ParamValidException(String code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

}
