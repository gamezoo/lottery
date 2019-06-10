package me.zohar.lottery.statisticalanalysis.vo;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.dictconfig.DictHolder;

@Data
public class AccountProfitAndLossVO {

	private String userAccountId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 账号类型
	 */
	private String accountType;

	private String accountTypeName;

	/**
	 * 账号级别
	 */
	private Integer accountLevel;

	/**
	 * 账号级别路径
	 */
	private String accountLevelPath;

	/**
	 * 余额
	 */
	private Double balance;

	private Double rechargeAmount;

	private Double withdrawAmount;

	private Double totalBettingAmount;

	private Double totalWinningAmount;

	private Double rebateAmount;

	private Double lowerLevelRebateAmount;

	private Double bettingProfitAndLoss;

	public static List<AccountProfitAndLossVO> convertFor(List<Object[]> result) {
		if (CollectionUtil.isEmpty(result)) {
			return new ArrayList<>();
		}
		List<AccountProfitAndLossVO> vos = new ArrayList<>();
		for (Object[] object : result) {
			vos.add(convertFor(object));
		}
		return vos;
	}

	public static AccountProfitAndLossVO convertFor(Object[] object) {
		if (object == null) {
			return null;
		}
		AccountProfitAndLossVO vo = new AccountProfitAndLossVO();
		vo.setUserAccountId((String) object[0]);
		vo.setUserName((String) object[1]);
		vo.setAccountType((String) object[2]);
		vo.setAccountLevel((Integer) object[3]);
		vo.setAccountLevelPath((String) object[4]);
		vo.setBalance((Double) object[5]);
		vo.setRechargeAmount((Double) object[6]);
		vo.setWithdrawAmount((Double) object[7]);
		vo.setTotalBettingAmount((Double) object[8]);
		vo.setTotalWinningAmount((Double) object[9]);
		vo.setRebateAmount((Double) object[10]);
		vo.setLowerLevelRebateAmount((Double) object[11]);
		vo.setBettingProfitAndLoss((Double) object[12]);
		vo.setAccountTypeName(DictHolder.getDictItemName("accountType", vo.getAccountType()));
		return vo;
	}

}
