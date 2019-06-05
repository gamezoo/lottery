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
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.domain.Issue;
import me.zohar.lottery.useraccount.domain.UserAccount;

/**
 * 投注订单
 * 
 * @author zohar
 * @date 2019年1月17日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "betting_order")
@DynamicInsert(true)
@DynamicUpdate(true)
public class BettingOrder {

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
	 * 投注时间
	 */
	private Date bettingTime;

	/**
	 * 撤单时间
	 */
	private Date cancelOrderTime;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 期号
	 */
	private Long issueNum;

	/**
	 * 全部开奖号码,以逗号分隔
	 */
	private String lotteryNum;

	/**
	 * 投注底数金额
	 */
	private Double baseAmount;

	/**
	 * 倍数
	 */
	private Double multiple;

	/**
	 * 总注数
	 */
	private Long totalBettingCount;

	/**
	 * 总投注金额
	 */
	private Double totalBettingAmount;

	/**
	 * 总中奖金额
	 */
	private Double totalWinningAmount;

	/**
	 * 总盈亏
	 */
	private Double totalProfitAndLoss;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 追号标识
	 */
	private Boolean trackingNumberFlag;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 返点金额
	 */
	private Double rebateAmount;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	@Column(name = "issue_id", length = 32)
	private String issueId;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Issue issue;

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
	@JoinColumn(name = "betting_order_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Set<BettingRecord> bettingRecords;

	public void cancelOrder() {
		this.setCancelOrderTime(new Date());
		this.setState(Constant.投注订单状态_已撤单);
	}

}
