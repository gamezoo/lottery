package me.zohar.lottery.useraccount.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 修改资金密码入参
 * @author zohar
 * @date 2019年1月19日
 *
 */
@Data
public class ModifyMoneyPwdParam {
	
	/**
	 * 旧的资金密码
	 */
	@NotBlank(message = "oldMoneyPwd不能为空")
	private String oldMoneyPwd;

	/**
	 * 新的资金密码
	 */
	@NotBlank(message = "newMoneyPwd不能为空")
	private String newMoneyPwd;

	/**
	 * 用户账号id
	 */
	@NotBlank(message = "userAccountId不能为空")
	private String userAccountId;

}
