package me.zohar.lottery.agent.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 邀请码
 * 
 * @author zohar
 * @date 2019年2月27日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "invite_code")
@DynamicInsert(true)
@DynamicUpdate(true)
public class InviteCode {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 邀请码
	 */
	private String code;

	/**
	 * 账号类型
	 */
	private String accountType;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 赔率
	 */
	private Double odds;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 有效期
	 */
	private Date periodOfValidity;

	/**
	 * 邀请人id
	 */
	@Column(name = "inviter_id", length = 32)
	private String inviterId;

	/**
	 * 邀请人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inviter_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount inviter;

}
