package me.zohar.lottery.betting.param;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 发起追号入参
 * 
 * @author zohar
 * @date 2019年1月17日
 *
 */
@Data
public class StartTrackingNumberParam {
	
	/**
	 * 游戏代码
	 */
	@NotBlank
	private String gameCode;
	
	/**
	 * 投注底数金额
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double baseAmount;
	
	/**
	 * 投注记录集合
	 */
	@NotEmpty(message = "bettingRecords不能为空")
	@Valid
	private List<BettingRecordParam> bettingRecords;

	/**
	 * 追号计划集合
	 */
	@Valid
	private List<TrackingNumberPlanParam> plans;

	/**
	 * 追号计划入参
	 * 
	 * @author zohar
	 * @date 2019年5月14日
	 *
	 */
	@Data
	public static class TrackingNumberPlanParam {

		/**
		 * 期号
		 */
		@NotNull
		private Long issueNum;

		/**
		 * 倍数
		 */
		@NotNull
		@DecimalMin(value = "1", inclusive = true)
		private Double multiple;
	}

}
