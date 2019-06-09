package me.zohar.lottery.betting.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.dictconfig.DictHolder;

/**
 * 投注订单信息
 * 
 * @author zohar
 * @date 2019年1月19日
 *
 */
@Data
public class BettingOrderInfoVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 投注时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date bettingTime;

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
	 * 总投注金额
	 */
	private Double totalBettingAmount;

	/**
	 * 总中奖金额
	 */
	private Double totalWinningAmount;

	/**
	 * 总盈亏
	 */
	private Double totalProfitAndLoss;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 状态名称
	 */
	private String stateName;

	/**
	 * 投注人用户账号id
	 */
	private String userAccountId;

	/**
	 * 投注人用户名
	 */
	private String userName;

	private Boolean cancelOrderFlag = false;

	public static List<BettingOrderInfoVO> convertFor(Collection<BettingOrder> bettingOrders) {
		if (CollectionUtil.isEmpty(bettingOrders)) {
			return new ArrayList<>();
		}
		List<BettingOrderInfoVO> vos = new ArrayList<>();
		for (BettingOrder bettingOrder : bettingOrders) {
			vos.add(convertFor(bettingOrder));
		}
		return vos;
	}

	public static BettingOrderInfoVO convertFor(BettingOrder bettingOrder) {
		if (bettingOrder == null) {
			return null;
		}
		BettingOrderInfoVO vo = new BettingOrderInfoVO();
		BeanUtils.copyProperties(bettingOrder, vo);
		vo.setGameName(DictHolder.getDictItemName("game", vo.getGameCode()));
		vo.setStateName(DictHolder.getDictItemName("bettingOrderState", vo.getState()));
		if (bettingOrder.getUserAccount() != null) {
			vo.setUserName(bettingOrder.getUserAccount().getUserName());
		}
		if (Constant.投注订单状态_未开奖.equals(bettingOrder.getState())) {
			if (bettingOrder.getIssue() != null) {
				if (new Date().getTime() < bettingOrder.getIssue().getEndTime().getTime()) {
					vo.setCancelOrderFlag(true);
				}
			}
		}
		return vo;
	}

}
