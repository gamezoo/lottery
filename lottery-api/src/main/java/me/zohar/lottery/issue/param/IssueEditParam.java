package me.zohar.lottery.issue.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 期号编辑入参
 * 
 * @author zohar
 * @date 2019年2月22日
 *
 */
@Data
public class IssueEditParam {

	/**
	 * 主键id
	 */
	@NotBlank
	private String id;

	/**
	 * 自动开奖
	 */
	@NotNull
	private Boolean automaticLottery;

	/**
	 * 自动结算
	 */
	@NotNull
	private Boolean automaticSettlement;

	/**
	 * 该期是否作废
	 */
	@NotNull
	private Boolean issueInvalid;

}
