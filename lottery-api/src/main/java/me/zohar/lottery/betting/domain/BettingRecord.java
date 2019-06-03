package me.zohar.lottery.betting.domain;

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

import lombok.Getter;
import lombok.Setter;

/**
 * 投注记录
 * 
 * @author zohar
 * @date 2018年12月25日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "betting_record")
@DynamicInsert(true)
@DynamicUpdate(true)
public class BettingRecord {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 游戏玩法类别代码
	 */
	private String gamePlayCategoryCode;

	/**
	 * 游戏玩法代码
	 */
	private String gamePlayCode;

	/**
	 * 所选号码
	 */
	private String selectedNo;

	/**
	 * 注数
	 */
	private Long bettingCount;

	/**
	 * 投注金额
	 */
	private Double bettingAmount;

	/**
	 * 赔率
	 */
	private Double odds;

	/**
	 * 中奖金额
	 */
	private Double winningAmount;

	/**
	 * 盈亏
	 */
	private Double profitAndLoss;

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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "betting_order_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private BettingOrder bettingOrder;

}
