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
import me.zohar.lottery.dictconfig.ConfigHolder;
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
	@NotBlank(message = "rechargeWayCode不能为空")
	private String rechargeWayCode;

	/**
	 * 充值金额
	 */
	@NotNull(message = "rechargeAmount不能为空")
	@DecimalMin(value = "0", inclusive = false, message = "rechargeAmount不能少于或等于0")
	private Double rechargeAmount;

	/**
	 * 用户账号id
	 */
	@NotBlank(message = "userAccountId不能为空")
	private String userAccountId;

	public RechargeOrder convertToPo() {
		RechargeOrder po = new RechargeOrder();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setSubmitTime(new Date());
		po.setUsefulTime(DateUtil.offset(po.getSubmitTime(), DateField.SECOND,
				Integer.parseInt(ConfigHolder.getConfigValue("recharge.effectiveDuration"))));
		po.setOrderNo(po.getId());
		po.setOrderState(Constant.充值订单状态_待支付);
		return po;
	}

}
