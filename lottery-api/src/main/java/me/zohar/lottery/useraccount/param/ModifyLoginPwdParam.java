package me.zohar.lottery.useraccount.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 修改登录密码入参
 * 
 * @author zohar
 * @date 2019年1月19日
 *
 */
@Data
public class ModifyLoginPwdParam {

	/**
	 * 旧的登录密码
	 */
	@NotBlank(message = "oldLoginPwd不能为空")
	private String oldLoginPwd;

	/**
	 * 新的登录密码
	 */
	@NotBlank(message = "newLoginPwd不能为空")
	private String newLoginPwd;

	/**
	 * 用户账号id
	 */
	@NotBlank(message = "userAccountId不能为空")
	private String userAccountId;

}
