package me.zohar.lottery.mastercontrol.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.lottery.mastercontrol.domain.RegisterAmountSetting;

@Data
public class RegisterAmountSettingVO {

	private String id;

	/**
	 * 启用标识
	 */
	private Boolean enabled;

	/**
	 * 注册礼金
	 */
	private Double registerAmount;
	
	/**
	 * 最近修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyUpdateTime;
	
	public static RegisterAmountSettingVO convertFor(RegisterAmountSetting registerAmountSetting) {
		if (registerAmountSetting == null) {
			return null;
		}
		RegisterAmountSettingVO vo = new RegisterAmountSettingVO();
		BeanUtils.copyProperties(registerAmountSetting, vo);
		return vo;
	}

}
