package me.zohar.lottery.betting.param;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.betting.enums.BettingOrderState;
import me.zohar.lottery.common.utils.IdUtils;

/**
 * 下单入参
 * 
 * @author zohar
 * @date 2019年1月17日
 *
 */
@Data
public class PlaceOrderParam {

	/**
	 * 游戏代码
	 */
	@NotBlank(message = "gameCode不能为空")
	private String gameCode;

	/**
	 * 期号
	 */
	@NotNull(message = "issueNum不能为空")
	private Long issueNum;

	/**
	 * 投注底数金额
	 */
	@NotNull(message = "baseAmount不能为空")
	@DecimalMin(value = "0", inclusive = false, message = "baseAmount不能少于或等于0")
	private Double baseAmount;

	/**
	 * 倍数
	 */
	@NotNull(message = "multiple不能为空")
	@DecimalMin(value = "1", inclusive = true, message = "multiple不能小于1")
	private Double multiple;

	/**
	 * 投注记录集合
	 */
	@NotEmpty(message = "bettingRecords不能为空")
	@Valid
	private List<BettingRecordParam> bettingRecords;

	public BettingOrder convertToPo(Long totalBettingCount, Double totalBettingAmount, String userAccountId) {
		BettingOrder po = new BettingOrder();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getGameCode() + po.getId());
		po.setBettingTime(new Date());
		po.setTotalBettingAmount(totalBettingAmount);
		po.setTotalBettingCount(totalBettingCount);
		po.setTotalWinningAmount(0d);
		po.setTotalProfitAndLoss(-totalBettingAmount);
		po.setState(BettingOrderState.投注订单状态_未开奖.getCode());
		po.setUserAccountId(userAccountId);
		return po;
	}

}
