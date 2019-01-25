package me.zohar.lottery.gd11x5.enums;

import java.util.Arrays;


import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 广东11选5玩法枚举类
 * 
 * @author zohar
 * @date 2018年12月26日
 *
 */
@Getter
@AllArgsConstructor
public enum Gd11x5Play {

	广东11选5_任选二中二单式("GD11X5_RX2Z2DS", "广东11选5_任选二中二单式") {

		/**
		 * 从01-11共11个号码中选择2个号码组成一注，只要当期开奖号码中包含所选号码，即中奖金。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：5,X,6,X,X 投注号码：5,6
		 */
		@Override
		public boolean isWinning(String lotteryNum, String selectedNo) {
			return CollUtil.intersection(Arrays.asList(lotteryNum.split(",")), Arrays.asList(selectedNo.split(",")))
					.size() == 2;
		}

	};

	private String code;

	private String name;

	/**
	 * 判断是否中奖
	 * 
	 * @param lotteryNum
	 * @param selectedNo
	 * @return
	 */
	public abstract boolean isWinning(String lotteryNum, String selectedNo);

	/**
	 * 根据code获取对应的玩法
	 * 
	 * @param code
	 * @return
	 */
	public static Gd11x5Play getPlay(String code) {
		for (Gd11x5Play play : Gd11x5Play.values()) {
			if (play.getCode().equals(code)) {
				return play;
			}
		}
		return null;
	}

}
