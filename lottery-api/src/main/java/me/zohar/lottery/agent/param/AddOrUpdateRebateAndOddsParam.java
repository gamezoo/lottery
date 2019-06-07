package me.zohar.lottery.agent.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.agent.domain.RebateAndOdds;
import me.zohar.lottery.common.utils.IdUtils;

@Data
public class AddOrUpdateRebateAndOddsParam {

	private String id;

	/**
	 * 返点
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double rebate;

	/**
	 * 赔率
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double odds;

	public RebateAndOdds convertToPo(Date createTime) {
		RebateAndOdds po = new RebateAndOdds();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(createTime);
		return po;
	}

}
