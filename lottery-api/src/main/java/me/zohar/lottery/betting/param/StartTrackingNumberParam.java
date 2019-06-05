package me.zohar.lottery.betting.param;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import me.zohar.lottery.betting.domain.TrackingNumberOrder;
import me.zohar.lottery.betting.domain.TrackingNumberPlan;
import me.zohar.lottery.common.utils.IdUtils;

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
	 * 返点
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double rebate;
	
	/**
	 * 中奖即停
	 */
	@NotNull
	private Boolean winToStop;

	/**
	 * 投注记录集合
	 */
	@NotEmpty
	@Valid
	private List<BettingRecordParam> bettingRecords;

	/**
	 * 追号计划集合
	 */
	@NotEmpty
	@Valid
	private List<TrackingNumberPlanParam> plans;

	public TrackingNumberOrder convertToPo(Long startIssueNum, Double totalBettingAmount, String userAccountId) {
		TrackingNumberOrder po = new TrackingNumberOrder();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setStartIssueNum(startIssueNum);
		po.setTrackingNumberTime(new Date());
		po.setTotalBettingAmount(NumberUtil.round(totalBettingAmount, 4).doubleValue());
		po.setUserAccountId(userAccountId);
		return po;
	}

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

		/**
		 * 投注订单id
		 */
		private String bettingOrderId;

		public TrackingNumberPlan convertToPo(String trackingNumberOrderId) {
			TrackingNumberPlan po = new TrackingNumberPlan();
			BeanUtils.copyProperties(this, po);
			po.setId(IdUtils.getId());
			po.setTrackingNumberOrderId(trackingNumberOrderId);
			return po;
		}
	}

}
