package me.zohar.lottery.useraccount.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.useraccount.domain.UserAccount;

@Data
public class AddUserAccountParam {

	/**
	 * 邀请人
	 */
	private String inviterUserName;

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
	private String accountType;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 登录密码
	 */
	@NotBlank
	private String loginPwd;

	public UserAccount convertToPo() {
		UserAccount po = new UserAccount();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setRegisteredTime(new Date());
		po.setMoneyPwd(po.getLoginPwd());
		return po;
	}

}
