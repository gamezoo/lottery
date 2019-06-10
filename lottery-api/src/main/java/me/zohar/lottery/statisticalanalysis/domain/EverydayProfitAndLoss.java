package me.zohar.lottery.statisticalanalysis.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 每日盈亏
 * 
 * @author zohar
 * @date 2019年6月10日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "v_everyday_profit_and_loss")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EverydayProfitAndLoss {

	/**
	 * 主键id
	 */
	@Id
	private String id;

	private String userAccountId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 账号类型
	 */
	private String accountType;

	/**
	 * 账号级别
	 */
	private Integer accountLevel;

	/**
	 * 账号级别路径
	 */
	private String accountLevelPath;

	/**
	 * 余额
	 */
	private Double balance;

	private Date everyday;

	private Double rechargeAmount;

	private Double withdrawAmount;

	private Double totalBettingAmount;

	private Double totalWinningAmount;

	private Double rebateAmount;

	private Double lowerLevelRebateAmount;

	private Double bettingProfitAndLoss;

}
