package me.zohar.lottery.betting.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.betting.domain.BettingRecord;
import me.zohar.lottery.betting.domain.TrackingNumberContent;
import me.zohar.lottery.common.utils.IdUtils;

/**
 * 投注记录入参
 * 
 * @author zohar
 * @date 2019年1月6日
 *
 */
@Data
public class BettingRecordParam {

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

	/**
	 * 注数
	 */
	@NotNull
	@DecimalMin(value = "1", inclusive = true)
	private Long bettingCount;

	public BettingRecord convertToPo(Double bettingAmount, Double odds) {
		BettingRecord po = new BettingRecord();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setBettingAmount(bettingAmount);
		po.setOdds(odds);
		po.setWinningAmount(0d);
		po.setProfitAndLoss(-bettingAmount);
		return po;
	}

	public TrackingNumberContent convertToTrackingNumberContentPo(String trackingNumberOrderId) {
		TrackingNumberContent po = new TrackingNumberContent();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setTrackingNumberOrderId(trackingNumberOrderId);
		return po;
	}

}
