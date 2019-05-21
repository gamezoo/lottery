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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * 追号计划
 * 
 * @author zohar
 * @date 2019年5月14日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "tracking_number_plan")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TrackingNumberPlan {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 期号
	 */
	private Long issueNum;

	/**
	 * 倍数
	 */
	private Double multiple;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	/**
	 * 追号订单id
	 */
	@Column(name = "tracking_number_order_id", length = 32)
	private String trackingNumberOrderId;

	/**
	 * 追号订单
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tracking_number_order_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TrackingNumberOrder trackingNumberOrder;

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

}
