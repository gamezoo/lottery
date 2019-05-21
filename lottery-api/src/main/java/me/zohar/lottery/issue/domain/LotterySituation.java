package me.zohar.lottery.issue.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 开奖情况实体
 * 
 * @author zohar
 * @date 2019年2月14日
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "v_lottery_situation")
public class LotterySituation {

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
	 * 总投注金额
	 */
	private Double totalBettingAmount;

	/**
	 * 总中奖金额
	 */
	private Double totalWinningAmount;

}
