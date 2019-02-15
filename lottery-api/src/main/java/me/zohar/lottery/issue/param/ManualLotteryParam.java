package me.zohar.lottery.issue.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ManualLotteryParam {

	/**
	 * 主键id
	 */
	@NotBlank
	private String id;

	/**
	 * 全部开奖号码,以逗号分隔
	 */
	@NotBlank
	private String lotteryNum;

	/**
	 * 自动结算标识
	 */
	@NotNull
	private Boolean autoSettlementFlag;

}
