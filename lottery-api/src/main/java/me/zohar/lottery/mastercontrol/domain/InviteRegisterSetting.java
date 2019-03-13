package me.zohar.lottery.mastercontrol.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zohar.lottery.common.utils.IdUtils;

/**
 * 邀请注册设置
 * 
 * @author zohar
 * @date 2019年3月12日
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invite_register_setting", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class InviteRegisterSetting {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
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
	private Date latelyUpdateTime;

	public void update(Integer effectiveDuration, Boolean enabled) {
		this.setEffectiveDuration(effectiveDuration);
		this.setEnabled(enabled);
		this.setLatelyUpdateTime(new Date());
	}

	public static InviteRegisterSetting build() {
		InviteRegisterSetting registerAmount = new InviteRegisterSetting();
		registerAmount.setId(IdUtils.getId());
		return registerAmount;
	}

}
