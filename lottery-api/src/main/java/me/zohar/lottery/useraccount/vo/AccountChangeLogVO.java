package me.zohar.lottery.useraccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.useraccount.domain.AccountChangeLog;

@Data
public class AccountChangeLogVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 期号
	 */
	private Long issueNum;

	/**
	 * 账变时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date accountChangeTime;

	/**
	 * 账变类型代码
	 */
	private String accountChangeTypeCode;

	/**
	 * 账变类型名称
	 */
	private String accountChangeTypeName;

	/**
	 * 账变金额
	 */
	private Double accountChangeAmount;

	/**
	 * 余额
	 */
	private Double balance;
	
	/**
	 * 备注
	 */
	private String note;

	/**
	 * 用户账号id
	 */
	private String userAccountId;

	/**
	 * 用户名
	 */
	private String userName;

	public static List<AccountChangeLogVO> convertFor(List<AccountChangeLog> accountChangeLogs) {
		if (CollectionUtil.isEmpty(accountChangeLogs)) {
			return new ArrayList<>();
		}
		List<AccountChangeLogVO> vos = new ArrayList<>();
		for (AccountChangeLog accountChangeLog : accountChangeLogs) {
			vos.add(convertFor(accountChangeLog));
		}
		return vos;
	}

	public static AccountChangeLogVO convertFor(AccountChangeLog accountChangeLog) {
		if (accountChangeLog == null) {
			return null;
		}
		AccountChangeLogVO vo = new AccountChangeLogVO();
		BeanUtils.copyProperties(accountChangeLog, vo);
		vo.setGameName(DictHolder.getDictItemName("game", vo.getGameCode()));
		if (accountChangeLog.getUserAccount() != null) {
			vo.setUserName(accountChangeLog.getUserAccount().getUserName());
		}
		vo.setAccountChangeTypeName(DictHolder.getDictItemName("accountChangeType", vo.getAccountChangeTypeCode()));
		return vo;
	}
}
