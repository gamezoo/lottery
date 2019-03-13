package me.zohar.lottery.rechargewithdraw.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.rechargewithdraw.domain.RechargeOrder;

/**
 * 充值订单入参
 * 
 * @author zohar
 * @date 2019年1月21日
 *
 */
@Data
public class RechargeOrderParam {

	/**
	 * 充值方式代码
	 */
	@NotBlank
	private String rechargeWayCode;

	/**
	 * 充值金额
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double rechargeAmount;

	/**
	 * 用户账号id
	 */
	@NotBlank
	private String userAccountId;

	/**
	 * 构建充值订单
	 * 
	 * @param orderEffectiveDuration
	 *            订单有效时长
	 * @return
	 */
	public RechargeOrder convertToPo(Integer orderEffectiveDuration) {
		RechargeOrder po = new RechargeOrder();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setSubmitTime(new Date());
		po.setUsefulTime(DateUtil.offset(po.getSubmitTime(), DateField.MINUTE, orderEffectiveDuration));
		po.setOrderNo(po.getId());
		po.setOrderState(Constant.充值订单状态_待支付);
		return po;
	}

}
