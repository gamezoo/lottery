package me.zohar.lottery.useraccount.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 登录账号信息,包含密码
 * 
 * @author zohar
 * @date 2019年1月25日
 *
 */
@Data
public class LoginAccountInfoVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 登录密码
	 */
	private String loginPwd;

	/**
	 * 账号类型
	 */
	private String accountType;

	private String accountTypeName;

	public static LoginAccountInfoVO convertFor(UserAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		LoginAccountInfoVO vo = new LoginAccountInfoVO();
		BeanUtils.copyProperties(userAccount, vo);
		vo.setAccountTypeName(DictHolder.getDictItemName("accountType", vo.getAccountType()));
		return vo;
	}

}
