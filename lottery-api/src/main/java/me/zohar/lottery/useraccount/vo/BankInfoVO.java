package me.zohar.lottery.useraccount.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 银行资料
 * 
 * @author zohar
 * @date 2019年1月20日
 *
 */
@Data
public class BankInfoVO {

	/**
	 * 开户银行
	 */
	private String openAccountBank;

	/**
	 * 开户人姓名
	 */
	private String accountHolder;

	/**
	 * 银行卡账号
	 */
	private String bankCardAccount;

	/**
	 * 银行资料最近修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date bankInfoLatelyModifyTime;

	public static BankInfoVO convertFor(UserAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		BankInfoVO vo = new BankInfoVO();
		BeanUtils.copyProperties(userAccount, vo);
		return vo;
	}

}
