package me.zohar.lottery.game.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_game_situation", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class GameSituation {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 游戏名称
	 */
	private String gameName;
	
	private String gameCategoryId;
	
	private Boolean hotGameFlag;

	/**
	 * 总期数
	 */
	private Long issueCount;

	/**
	 * 当前期
	 */
	private Long currentIssue;

	private Long currentIssueInner;

	/**
	 * 当前期结束时间
	 */
	private Date currentIssueEndTime;

	/**
	 * 上一期
	 */
	private Long preIssue;

	/**
	 * 上一期开奖号码
	 */
	private String preIssueLotteryNum;

	/**
	 * 下一期
	 */
	private Long nextIssue;

	/**
	 * 下一期结束时间
	 */
	private Date nextIssueEndTime;

}
