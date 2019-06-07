package me.zohar.lottery.agent.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class AgentOpenAnAccountParam {

	/**
	 * 用户名
	 */
	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{5,11}$")
	private String userName;

	/**
	 * 登录密码
	 */
	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{5,14}$")
	private String loginPwd;

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
	@DecimalMin(value = "0", inclusive = true)
	private Double odds;

	/**
	 * 邀请人账号id
	 */
	private String inviterId;

}
