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
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 充值订单
 * 
 * @author zohar
 * @date 2019年1月21日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "recharge_order", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class RechargeOrder {

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
	 * 充值方式代码
	 */
	private String rechargeWayCode;

	/**
	 * 充值金额
	 */
	private Double rechargeAmount;

	/**
	 * 实际支付金额
	 */
	private Double actualPayAmount;

	/**
	 * 提交时间
	 */
	private Date submitTime;

	/**
	 * 有效时间
	 */
	private Date usefulTime;

	/**
	 * 订单状态
	 */
	private String orderState;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 支付地址
	 */
	private String payUrl;

	/**
	 * 支付时间
	 */
	private Date payTime;

	/**
	 * 处理时间
	 */
	private Date dealTime;

	/**
	 * 结算时间,即更新到账号余额的时间
	 */
	private Date settlementTime;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	/**
	 * 用户账号id
	 */
	@Column(name = "user_account_id", length = 32)
	private String userAccountId;

	/**
	 * 用户账号
	 */
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_account_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount userAccount;

	/**
	 * 更新支付信息
	 * 
	 * @param actualPayAmount
	 * @param payTime
	 */
	public void updatePayInfo(Double actualPayAmount, Date payTime) {
		this.setActualPayAmount(actualPayAmount);
		this.setPayTime(payTime);
		this.setDealTime(new Date());
		this.setOrderState(Constant.充值订单状态_已支付);
	}

	/**
	 * 订单结算
	 */
	public void settlement() {
		this.setSettlementTime(new Date());
		this.setOrderState(Constant.充值订单状态_已结算);
	}

}
