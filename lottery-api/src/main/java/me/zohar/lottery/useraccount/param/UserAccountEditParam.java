package me.zohar.lottery.useraccount.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserAccountEditParam {

	/**
	 * 主键id
	 */
	@NotBlank
	private String userAccountId;

	/**
	 * 用户名
	 */
	@NotBlank
	private String userName;

	/**
	 * 真实姓名
	 */
	@NotBlank
	private String realName;

	/**
	 * 账号类型
	 */
	@NotBlank
	private String accountType;
	
	/**
	 * 返点
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double rebate;
	
	/**
	 * 赔率
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double odds;

	/**
	 * 状态
	 */
	@NotBlank
	private String state;

}
