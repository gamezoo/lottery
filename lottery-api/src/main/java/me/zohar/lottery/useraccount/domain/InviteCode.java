package me.zohar.lottery.useraccount.domain;

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

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;
import me.zohar.lottery.common.utils.IdUtils;

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
@Table(name = "invite_code", schema = "lottery")
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
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 有效期
	 */
	private Date periodOfValidity;

	/**
	 * 用户账号id
	 */
	@Column(name = "user_account_id", length = 32)
	private String userAccountId;

	/**
	 * 用户账号
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_account_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount userAccount;

	public static InviteCode generateInviteCode(String code, Integer effectiveDuration, String userAccountId) {
		InviteCode inviteCode = new InviteCode();
		inviteCode.setId(IdUtils.getId());
		inviteCode.setCode(code);
		inviteCode.setCreateTime(new Date());
		inviteCode.setPeriodOfValidity(
				DateUtil.offset(inviteCode.getCreateTime(), DateField.DAY_OF_YEAR, effectiveDuration));
		inviteCode.setUserAccountId(userAccountId);
		return inviteCode;
	}

}
