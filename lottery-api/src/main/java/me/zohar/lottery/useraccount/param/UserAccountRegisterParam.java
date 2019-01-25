package me.zohar.lottery.useraccount.param;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.useraccount.domain.UserAccount;

@Data
public class UserAccountRegisterParam {

	private String userName;

	private String realName;

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
