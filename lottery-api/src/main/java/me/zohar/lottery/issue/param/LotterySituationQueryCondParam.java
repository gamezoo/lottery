package me.zohar.lottery.issue.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class LotterySituationQueryCondParam extends PageParam {

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 期号
	 */
	private Long issueNum;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 开奖开始日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date lotteryStartDate;

	/**
	 * 开奖结束日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date lotteryEndDate;

}
