package me.zohar.lottery.useraccount.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.useraccount.domain.UserAccount;

@Data
public class UserAccountRegisterParam {

	/**
	 * 邀请码
	 */
	private String inviteCode;

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
		po.setState(Constant.账号状态_启用);
		po.setAccountType(Constant.账号类型_会员);
		return po;
	}

}
