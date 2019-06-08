package me.zohar.lottery.agent.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.zohar.lottery.agent.domain.InviteCode;
import me.zohar.lottery.common.utils.IdUtils;

@Data
public class GenerateInviteCodeParam {

	@NotBlank
	private String accountType;

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

	/**
	 * 邀请人账号id
	 */
	@NotBlank
	private String inviterId;

	public InviteCode convertToPo(String code, Integer effectiveDuration) {
		InviteCode po = new InviteCode();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCode(code);
		po.setCreateTime(new Date());
		po.setPeriodOfValidity(DateUtil.offset(po.getCreateTime(), DateField.DAY_OF_YEAR, effectiveDuration));
		return po;
	}

}
