package me.zohar.lottery.rechargewithdraw.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 充值订单状态枚举类
 * 
 * @author zohar
 * @date 2018年12月29日
 *
 */
@Getter
@AllArgsConstructor
public enum RechargeOrderState {

	充值订单状态_待支付("1", "待支付"),

	充值订单状态_已支付("2", "已支付"),

	充值订单状态_超时取消("3", "超时取消");

	private String code;

	private String name;

	public static RechargeOrderState getState(String code) {
		for (RechargeOrderState state : RechargeOrderState.values()) {
			if (state.getCode().equals(code)) {
				return state;
			}
		}
		return null;
	}

}
