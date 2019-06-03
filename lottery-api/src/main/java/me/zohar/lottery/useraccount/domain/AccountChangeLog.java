package me.zohar.lottery.useraccount.domain;

import java.text.MessageFormat;
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

import cn.hutool.core.util.NumberUtil;
import lombok.Getter;
import lombok.Setter;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.betting.domain.BettingRebate;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.mastercontrol.domain.RegisterAmountSetting;
import me.zohar.lottery.rechargewithdraw.domain.RechargeOrder;
import me.zohar.lottery.rechargewithdraw.domain.WithdrawRecord;

/**
 * 账变日志
 * 
 * @author zohar
 * @date 2019年1月17日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "account_change_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AccountChangeLog {

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
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 期号
	 */
	private Long issueNum;

	/**
	 * 账变时间
	 */
	private Date accountChangeTime;

	/**
	 * 账变类型代码
	 */
	private String accountChangeTypeCode;

	/**
	 * 账变金额
	 */
	private Double accountChangeAmount;

	/**
	 * 余额
	 */
	private Double balance;

	/**
	 * 备注
	 */
	private String note;

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
	 * 构建投注返点账变日志
	 * 
	 * @param userAccount
	 * @param bettingRebate
	 * @return
	 */
	public static AccountChangeLog buildWithBettingRebate(UserAccount userAccount, BettingRebate bettingRebate) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(bettingRebate.getId());
		log.setGameCode(bettingRebate.getBettingOrder().getGameCode());
		log.setIssueNum(bettingRebate.getBettingOrder().getIssueNum());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(
				bettingRebate.getWinningRebateFlag() ? Constant.账变日志类型_中奖返点 : Constant.账变日志类型_投注返点);
		log.setAccountChangeAmount(NumberUtil.round(bettingRebate.getRebateAmount(), 4).doubleValue());
		log.setBalance(userAccount.getBalance());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建注册礼金账变日志
	 * 
	 * @param userAccount
	 * @param setting
	 * @return
	 */
	public static AccountChangeLog buildWithRegisterAmount(UserAccount userAccount, RegisterAmountSetting setting) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(log.getId());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_活动礼金);
		log.setAccountChangeAmount(NumberUtil.round(setting.getRegisterAmount(), 4).doubleValue());
		log.setBalance(userAccount.getBalance());
		log.setNote("新用户注册礼金");
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建充值账变日志
	 * 
	 * @param userAccount
	 * @param rechargeOrder
	 * @return
	 */
	public static AccountChangeLog buildWithRecharge(UserAccount userAccount, RechargeOrder rechargeOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(rechargeOrder.getOrderNo());
		log.setAccountChangeTime(rechargeOrder.getSettlementTime());
		log.setAccountChangeTypeCode(Constant.账变日志类型_账号充值);
		log.setAccountChangeAmount(NumberUtil.round(rechargeOrder.getActualPayAmount(), 4).doubleValue());
		log.setBalance(userAccount.getBalance());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建充值优惠账变日志
	 * 
	 * @param userAccount
	 * @param returnWater
	 * @param returnWaterRate
	 * @return
	 */
	public static AccountChangeLog buildWithRechargePreferential(UserAccount userAccount, double returnWater,
			Integer returnWaterRate) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(log.getId());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_充值优惠);
		log.setAccountChangeAmount(NumberUtil.round(returnWater, 4).doubleValue());
		log.setBalance(userAccount.getBalance());
		log.setNote(MessageFormat.format("充值返水率:{0}%", 5));
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建中奖账变日志
	 * 
	 * @param userAccount
	 * @param bettingOrder
	 * @return
	 */
	public static AccountChangeLog buildWithWinning(UserAccount userAccount, BettingOrder bettingOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(bettingOrder.getOrderNo());
		log.setGameCode(bettingOrder.getGameCode());
		log.setIssueNum(bettingOrder.getIssueNum());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_投注返奖);
		log.setAccountChangeAmount(NumberUtil.round(bettingOrder.getTotalWinningAmount(), 4).doubleValue());
		log.setBalance(userAccount.getBalance());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建下单账变日志
	 * 
	 * @param userAccount
	 * @param bettingOrder
	 * @return
	 */
	public static AccountChangeLog buildWithPlaceOrder(UserAccount userAccount, BettingOrder bettingOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(bettingOrder.getOrderNo());
		log.setGameCode(bettingOrder.getGameCode());
		log.setIssueNum(bettingOrder.getIssueNum());
		log.setAccountChangeTime(bettingOrder.getBettingTime());
		log.setAccountChangeTypeCode(Constant.账变日志类型_投注扣款);
		log.setAccountChangeAmount(-bettingOrder.getTotalBettingAmount());
		log.setBalance(userAccount.getBalance());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建发起提现账变日志
	 * 
	 * @param userAccount
	 * @param withdrawRecord
	 * @return
	 */
	public static AccountChangeLog buildWithStartWithdraw(UserAccount userAccount, WithdrawRecord withdrawRecord) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(withdrawRecord.getOrderNo());
		log.setAccountChangeTime(withdrawRecord.getSubmitTime());
		log.setAccountChangeTypeCode(Constant.账变日志类型_账号提现);
		log.setAccountChangeAmount(NumberUtil.round(withdrawRecord.getWithdrawAmount(), 4).doubleValue());
		log.setBalance(userAccount.getBalance());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

	/**
	 * 构建投注撤单账变日志
	 * 
	 * @param userAccount
	 * @param bettingOrder
	 * @return
	 */
	public static AccountChangeLog buildWithBettingCancelOrder(UserAccount userAccount, BettingOrder bettingOrder) {
		AccountChangeLog log = new AccountChangeLog();
		log.setId(IdUtils.getId());
		log.setOrderNo(bettingOrder.getOrderNo());
		log.setGameCode(bettingOrder.getGameCode());
		log.setIssueNum(bettingOrder.getIssueNum());
		log.setAccountChangeTime(new Date());
		log.setAccountChangeTypeCode(Constant.账变日志类型_撤单返款);
		log.setAccountChangeAmount(bettingOrder.getTotalBettingAmount());
		log.setBalance(userAccount.getBalance());
		log.setUserAccountId(userAccount.getId());
		return log;
	}

}
