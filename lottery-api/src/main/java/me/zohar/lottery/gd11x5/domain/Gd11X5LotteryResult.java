package me.zohar.lottery.gd11x5.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

/**
 * 广东11选5开奖结果
 * 
 * @author zohar
 * @date 2018年12月23日
 *
 */
@Data
@Entity
@Table(name = "gd11x5_lottery_result", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Gd11X5LotteryResult {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 期号
	 */
	private Long issue;
	
	/**
	 * 开奖日期
	 */
	private Date lotteryDate;
	
	/**
	 * 全部开奖号码,以逗号分隔
	 */
	private String lotteryNum;

	/**
	 * 开奖号码第1位
	 */
	private Integer lotteryNum1;

	/**
	 * 开奖号码第2位
	 */
	private Integer lotteryNum2;

	/**
	 * 开奖号码第3位
	 */
	private Integer lotteryNum3;

	/**
	 * 开奖号码第4位
	 */
	private Integer lotteryNum4;

	/**
	 * 开奖号码第5位
	 */
	private Integer lotteryNum5;
	
	/**
	 * 同步时间
	 */
	private Date syncTime;
	
	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

}
