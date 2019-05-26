package me.zohar.lottery.betting.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.dictconfig.DictHolder;

@Data
public class WinningRankVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private String id;

	private String gameName;

	/**
	 * 期号
	 */
	private Long issueNum;

	private String userName;

	/**
	 * 总中奖金额
	 */
	private Double totalWinningAmount;

	public static List<WinningRankVO> convertFor(Collection<BettingOrder> bettingOrders) {
		if (CollectionUtil.isEmpty(bettingOrders)) {
			return new ArrayList<>();
		}
		List<WinningRankVO> vos = new ArrayList<>();
		for (BettingOrder bettingOrder : bettingOrders) {
			vos.add(convertFor(bettingOrder));
		}
		return vos;
	}

	public static WinningRankVO convertFor(BettingOrder bettingOrder) {
		if (bettingOrder == null) {
			return null;
		}
		WinningRankVO vo = new WinningRankVO();
		BeanUtils.copyProperties(bettingOrder, vo);
		vo.setGameName(DictHolder.getDictItemName("game", bettingOrder.getGameCode()));
		if (bettingOrder.getUserAccount() != null) {
			String userName = bettingOrder.getUserAccount().getUserName();
			userName = userName.substring(0, userName.length() - 4) + "****";
			vo.setUserName(userName);
		}
		return vo;
	}

}
