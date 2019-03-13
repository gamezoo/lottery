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
 * 注册礼金设置
 * 
 * @author zohar
 * @date 2019年3月10日
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "register_amount_setting", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class RegisterAmountSetting {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
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
	private Date latelyUpdateTime;

	public void update(Double registerAmount, Boolean enabled) {
		this.setRegisterAmount(registerAmount);
		this.setEnabled(enabled);
		this.setLatelyUpdateTime(new Date());
	}

	public static RegisterAmountSetting build() {
		RegisterAmountSetting registerAmount = new RegisterAmountSetting();
		registerAmount.setId(IdUtils.getId());
		return registerAmount;
	}

}
