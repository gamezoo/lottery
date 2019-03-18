package me.zohar.lottery.issue.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zohar.lottery.constants.Constant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "issue", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Issue {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 所属游戏代码
	 */
	private String gameCode;

	/**
	 * 期数
	 */
	private Long issueNum;
	
	/**
	 * 期数(内部用)
	 */
	private Long issueNumInner;

	/**
	 * 开奖日期,0点整
	 */
	private Date lotteryDate;

	/**
	 * 开奖时间
	 */
	private Date lotteryTime;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 全部开奖号码,以逗号分隔
	 */
	private String lotteryNum;

	/**
	 * 同步时间
	 */
	private Date syncTime;

	/**
	 * 结算时间
	 */
	private Date settlementTime;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 自动开奖
	 */
	private Boolean automaticLottery;

	/**
	 * 自动结算
	 */
	private Boolean automaticSettlement;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	/**
	 * 同步开奖号码
	 */
	public void syncLotteryNum(String lotteryNum) {
		this.setLotteryNum(lotteryNum);
		this.setSyncTime(new Date());
		this.setState(Constant.期号状态_已开奖);
	}

	/**
	 * 结算
	 */
	public void settlement() {
		this.setSettlementTime(new Date());
		this.setState(Constant.期号状态_已结算);
	}

	public void updateIssue(String state, Boolean automaticLottery, Boolean automaticSettlement) {
		this.setState(state);
		this.setAutomaticLottery(automaticLottery);
		this.setAutomaticSettlement(automaticSettlement);
	}

}
