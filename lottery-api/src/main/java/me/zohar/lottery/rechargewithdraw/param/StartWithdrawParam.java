package me.zohar.lottery.rechargewithdraw.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.rechargewithdraw.domain.WithdrawRecord;

/**
 * 发起提现入参
 * 
 * @author zohar
 * @date 2019年2月23日
 *
 */
@Data
public class StartWithdrawParam {

	/**
	 * 提现金额
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false, message = "withdrawAmount不能少于或等于0")
	private Double withdrawAmount;

	/**
	 * 资金密码
	 */
	@NotBlank
	private String moneyPwd;

	/**
	 * 用户账号id
	 */
	@NotBlank
	private String userAccountId;

	public WithdrawRecord convertToPo() {
		WithdrawRecord po = new WithdrawRecord();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setSubmitTime(new Date());
		po.setState(Constant.提现记录状态_发起提现);
		return po;
	}

}
