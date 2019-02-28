package me.zohar.lottery.useraccount.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.lottery.useraccount.domain.InviteCode;

@Data
public class InviteDetailsInfoVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 邀请码
	 */
	private String code;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 有效期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date periodOfValidity;

	/**
	 * 已邀请人数
	 */
	private Long numberOfInvite;

	/**
	 * 有效标识
	 */
	private Boolean validFlag;

	public static InviteDetailsInfoVO convertFor(InviteCode inviteCode, Long numberOfInvite) {
		if (inviteCode == null) {
			return null;
		}
		InviteDetailsInfoVO vo = new InviteDetailsInfoVO();
		BeanUtils.copyProperties(inviteCode, vo);
		vo.setNumberOfInvite(numberOfInvite);
		vo.setValidFlag(vo.getPeriodOfValidity().getTime() > new Date().getTime());
		return vo;
	}

}
