package me.zohar.lottery.mastercontrol.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.lottery.mastercontrol.domain.InviteRegisterSetting;

@Data
public class InviteRegisterSettingVO {

	private String id;
	
	/**
	 * 邀请码有效时长
	 */
	private Integer effectiveDuration;

	/**
	 * 启用标识
	 */
	private Boolean enabled;
	
	/**
	 * 最近修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyUpdateTime;
	
	public static InviteRegisterSettingVO convertFor(InviteRegisterSetting inviteRegisterSetting) {
		if (inviteRegisterSetting == null) {
			return null;
		}
		InviteRegisterSettingVO vo = new InviteRegisterSettingVO();
		BeanUtils.copyProperties(inviteRegisterSetting, vo);
		return vo;
	}

}
