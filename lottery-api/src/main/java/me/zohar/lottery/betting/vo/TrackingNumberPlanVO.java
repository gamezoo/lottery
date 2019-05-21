package me.zohar.lottery.betting.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.betting.domain.TrackingNumberPlan;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.dictconfig.DictHolder;

@Data
public class TrackingNumberPlanVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 期号
	 */
	private Long issueNum;

	/**
	 * 倍数
	 */
	private Double multiple;

	/**
	 * 全部开奖号码,以逗号分隔
	 */
	private String lotteryNum;

	/**
	 * 总投注金额
	 */
	private Double totalBettingAmount;

	/**
	 * 总中奖金额
	 */
	private Double totalWinningAmount;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 状态名称
	 */
	private String stateName;

	private String bettingOrderId;

	private Boolean cancelOrderFlag = false;

	public static List<TrackingNumberPlanVO> convertFor(Collection<TrackingNumberPlan> trackingNumberPlans) {
		if (CollectionUtil.isEmpty(trackingNumberPlans)) {
			return new ArrayList<>();
		}
		List<TrackingNumberPlanVO> vos = new ArrayList<>();
		for (TrackingNumberPlan trackingNumberPlan : trackingNumberPlans) {
			vos.add(convertFor(trackingNumberPlan));
		}
		return vos;
	}

	public static TrackingNumberPlanVO convertFor(TrackingNumberPlan trackingNumberPlan) {
		if (trackingNumberPlan == null) {
			return null;
		}
		TrackingNumberPlanVO vo = new TrackingNumberPlanVO();
		BeanUtils.copyProperties(trackingNumberPlan, vo);
		BettingOrder bettingOrder = trackingNumberPlan.getBettingOrder();
		if (bettingOrder != null) {
			vo.setLotteryNum(bettingOrder.getLotteryNum());
			vo.setTotalBettingAmount(bettingOrder.getTotalBettingAmount());
			vo.setTotalWinningAmount(bettingOrder.getTotalWinningAmount());
			vo.setState(bettingOrder.getState());
			vo.setStateName(DictHolder.getDictItemName("bettingOrderState", vo.getState()));
			vo.setBettingOrderId(bettingOrder.getId());
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
