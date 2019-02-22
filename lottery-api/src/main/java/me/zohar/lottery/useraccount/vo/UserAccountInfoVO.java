package me.zohar.lottery.useraccount.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 用户账号信息vo
 * 
 * @author zohar
 * @date 2018年12月27日
 *
 */
@Data
public class UserAccountInfoVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 余额
	 */
	private Double balance;

	public static UserAccountInfoVO convertFor(UserAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		UserAccountInfoVO vo = new UserAccountInfoVO();
		BeanUtils.copyProperties(userAccount, vo);
		return vo;
	}

}
