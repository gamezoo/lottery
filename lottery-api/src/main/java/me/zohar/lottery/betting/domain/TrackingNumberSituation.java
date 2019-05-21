package me.zohar.lottery.betting.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 追号情况
 * 
 * @author zohar
 * @date 2019年5月15日
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "v_tracking_number_situation")
public class TrackingNumberSituation {

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
	 * 追号时间
	 */
	private Date trackingNumberTime;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 开始期号
	 */
	private Long startIssueNum;
	
	/**
	 * 投注底数金额
	 */
	private Double baseAmount;

	/**
	 * 追号即停
	 */
	private Boolean winToStop;

	/**
	 * 追号期数
	 */
	private Long totalIssueCount;

	/**
	 * 总投注金额
	 */
	private Double totalBettingAmount;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 完成期数
	 */
	private Integer completedIssueCount;
	
	/**
	 * 未完成的期数(状态为未开奖且未到投注截至时间)
	 */
	private Integer uncompletedIssueCount;

	/**
	 * 投注人用户账号id
	 */
	@Column(name = "user_account_id", length = 32)
	private String userAccountId;

	/**
	 * 投注人用户账号
	 */
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_account_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount userAccount;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "tracking_number_order_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("issueNum ASC")
	private Set<TrackingNumberPlan> trackingNumberPlans;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "tracking_number_order_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Set<TrackingNumberContent> trackingNumberContents;

}
