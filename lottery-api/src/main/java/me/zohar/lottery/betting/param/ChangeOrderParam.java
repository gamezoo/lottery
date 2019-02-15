package me.zohar.lottery.betting.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ChangeOrderParam {

	/**
	 * 投注订单id
	 */
	@NotBlank
	private String bettingOrderId;

	/**
	 * 投注记录id
	 */
	@NotBlank
	private String bettingRecordId;

	/**
	 * 游戏玩法代码
	 */
	@NotBlank
	private String gamePlayCode;

	/**
	 * 所选号码
	 */
	@NotBlank
	private String selectedNo;

}
