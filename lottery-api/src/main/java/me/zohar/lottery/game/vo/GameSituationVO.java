package me.zohar.lottery.game.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.game.domain.GameSituation;

@Data
public class GameSituationVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 游戏名称
	 */
	private String gameName;

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date nextIssueEndTime;

	public static List<GameSituationVO> convertFor(Collection<GameSituation> hotGames) {
		if (CollectionUtil.isEmpty(hotGames)) {
			return new ArrayList<>();
		}
		List<GameSituationVO> vos = new ArrayList<>();
		for (GameSituation hotGame : hotGames) {
			vos.add(convertFor(hotGame));
		}
		return vos;
	}

	public static GameSituationVO convertFor(GameSituation hotGame) {
		if (hotGame == null) {
			return null;
		}
		GameSituationVO vo = new GameSituationVO();
		BeanUtils.copyProperties(hotGame, vo);
		return vo;
	}

}
