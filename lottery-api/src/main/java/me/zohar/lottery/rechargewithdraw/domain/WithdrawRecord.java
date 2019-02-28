package me.zohar.lottery.rechargewithdraw.domain;

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
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 提现记录
 * 
 * @author zohar
 * @date 2019年2月23日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "withdraw_record", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class WithdrawRecord {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 提现金额
	 */
	private Double withdrawAmount;

	/**
	 * 开户银行
	 */
	private String openAccountBank;

	/**
	 * 开户人姓名
	 */
	private String accountHolder;

	/**
	 * 银行卡账号
	 */
	private String bankCardAccount;

	/**
	 * 提交时间
	 */
	private Date submitTime;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 审核时间
	 */
	private Date approvalTime;

	/**
	 * 确认到帐时间
	 */
	private Date confirmCreditedTime;

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

	/**
	 * 设置银行卡信息
	 */
	public void setBankInfo(UserAccount userAccount) {
		this.setOpenAccountBank(userAccount.getOpenAccountBank());
		this.setAccountHolder(userAccount.getAccountHolder());
		this.setBankCardAccount(userAccount.getBankCardAccount());
	}

	public void approved(String note) {
		this.setState(Constant.提现记录状态_审核通过);
		this.setApprovalTime(new Date());
		this.setNote(note);
	}

	public void notApproved(String note) {
		this.setState(Constant.提现记录状态_审核不通过);
		this.setApprovalTime(new Date());
		this.setNote(note);
	}

	public void confirmCredited() {
		this.setState(Constant.提现记录状态_已到账);
		this.setConfirmCreditedTime(new Date());
	}

}
