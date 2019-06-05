package me.zohar.lottery.betting.domain;

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
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 投注返点
 * 
 * @author zohar
 * @date 2019年6月2日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "betting_rebate")
@DynamicInsert(true)
@DynamicUpdate(true)
public class BettingRebate {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 赔率
	 */
	private Double odds;

	/**
	 * 是否属于中奖返点
	 */
	private Boolean winningRebateFlag;

	/**
	 * 返点金额
	 */
	private Double rebateAmount;

	/**
	 * 结算时间,即更新到账号余额的时间
	 */
	private Date settlementTime;

	private Date createTime;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	/**
	 * 投注订单id
	 */
	@Column(name = "betting_order_id", length = 32)
	private String bettingOrderId;

	/**
	 * 投注订单
	 */
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "betting_order_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private BettingOrder bettingOrder;

	/**
	 * 返点账号id
	 */
	@Column(name = "rebate_account_id", length = 32)
	private String rebateAccountId;

	/**
	 * 返点账号
	 */
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rebate_account_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount rebateAccount;

	public void settlement() {
		this.setSettlementTime(new Date());
	}

	public static BettingRebate build(Double rebate, Boolean winningRebateFlag, Double rebateAmount,
			String bettingOrderId, String rebateAccountId) {
		BettingRebate bettingRebate = new BettingRebate();
		bettingRebate.setId(IdUtils.getId());
		bettingRebate.setCreateTime(new Date());
		bettingRebate.setRebate(rebate);
		bettingRebate.setWinningRebateFlag(winningRebateFlag);
		bettingRebate.setRebateAmount(rebateAmount);
		bettingRebate.setBettingOrderId(bettingOrderId);
		bettingRebate.setRebateAccountId(rebateAccountId);
		return bettingRebate;
	}

}
