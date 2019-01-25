package me.zohar.lottery.betting.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.betting.domain.BettingRecord;
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
	 * 游戏玩法类别代码
	 */
	@NotBlank(message = "gamePlayCategoryCode不能为空")
	private String gamePlayCategoryCode;

	/**
	 * 游戏玩法代码
	 */
	@NotBlank(message = "gamePlayCode不能为空")
	private String gamePlayCode;

	/**
	 * 所选号码
	 */
	@NotBlank(message = "selectedNo不能为空")
	private String selectedNo;

	/**
	 * 注数
	 */
	@NotNull(message = "bettingCount不能为空")
	@DecimalMin(value = "1", inclusive = true, message = "bettingCount不能小于1")
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

}
