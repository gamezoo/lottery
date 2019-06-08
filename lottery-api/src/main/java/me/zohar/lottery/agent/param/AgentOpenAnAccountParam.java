package me.zohar.lottery.agent.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.useraccount.domain.UserAccount;

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
	@NotBlank
	private String inviterId;

	public UserAccount convertToPo(Integer accountLevel) {
		UserAccount po = new UserAccount();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setAccountType(Constant.账号类型_代理);
		po.setAccountLevel(accountLevel);
		po.setMoneyPwd(po.getLoginPwd());
		po.setBalance(0d);
		po.setState(Constant.账号状态_启用);
		po.setRegisteredTime(new Date());
		return po;
	}

}
