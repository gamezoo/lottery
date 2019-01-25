package me.zohar.lottery.common.vo;

import java.io.Serializable;

/**
 * 前后端交互数据标准
 * 
 * @author zohar
 * @date 2018年12月27日
 *
 * @param <T>
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	private boolean success;

	/**
	 * 失败消息
	 */
	private String msg;

	/**
	 * 返回代码
	 */
	private Integer code;

	/**
	 * 时间戳
	 */
	private long timestamp = System.currentTimeMillis();

	/**
	 * 返回的数据
	 */
	private Object data;

	public static Result success() {
		Result result = new Result();
		result.setSuccess(true);
		result.setCode(200);
		result.setMsg("success");
		return result;
	}

	/**
	 * 
	 * @param errorMsg
	 * @return
	 */
	public static Result fail(String errorMsg) {
		Result result = new Result();
		result.setSuccess(false);
		result.setCode(500);
		result.setMsg(errorMsg);
		return result;
	}

	public static Result fail(Integer code, String msg) {
		Result result = new Result();
		result.setSuccess(false);
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}

	public boolean isSuccess() {
		return success;
	}

	public Result setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public Result setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public Result setCode(Integer code) {
		this.code = code;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Result setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public Object getData() {
		return data;
	}

	public Result setData(Object data) {
		this.data = data;
		return this;
	}

}
