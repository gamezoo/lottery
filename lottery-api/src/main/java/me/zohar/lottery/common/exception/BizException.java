package me.zohar.lottery.common.exception;

import lombok.Getter;

/**
 * 自定义业务异常
 * 
 * @author zohar
 * @date 2019年1月7日
 *
 */
@Getter
public class BizException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	private String msg;

	public BizException(BizError bizError) {
		super(bizError.getMsg());
		this.code = bizError.getCode();
		this.msg = bizError.getMsg();
	}

	public BizException(String code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

}
