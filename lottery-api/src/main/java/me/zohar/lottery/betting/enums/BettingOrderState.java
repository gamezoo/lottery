package me.zohar.lottery.betting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 投注订单状态枚举类
 * 
 * @author zohar
 * @date 2018年12月29日
 *
 */
@Getter
@AllArgsConstructor
public enum BettingOrderState {

	投注订单状态_未开奖("1", "未开奖"),

	投注订单状态_未中奖("2", "未中奖"),

	投注订单状态_已中奖("3", "已中奖");

	private String code;

	private String name;

	public static BettingOrderState getState(String code) {
		for (BettingOrderState state : BettingOrderState.values()) {
			if (state.getCode().equals(code)) {
				return state;
			}
		}
		return null;
	}

}
