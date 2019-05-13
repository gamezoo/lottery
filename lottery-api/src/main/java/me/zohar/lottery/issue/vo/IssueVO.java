package me.zohar.lottery.issue.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.issue.domain.Issue;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 所属游戏代码
	 */
	private String gameCode;

	/**
	 * 所属游戏名称
	 */
	private String gameName;

	/**
	 * 期数
	 */
	private Long issueNum;

	/**
	 * 开奖日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date lotteryDate;

	/**
	 * 开奖时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lotteryTime;

	/**
	 * 开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	/**
	 * 全部开奖号码,以逗号分隔
	 */
	private String lotteryNum;

	/**
	 * 同步时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date syncTime;

	/**
	 * 状态
	 */
	private String state;

	private String stateName;

	public static List<IssueVO> convertFor(List<Issue> issues) {
		if (CollectionUtil.isEmpty(issues)) {
			return new ArrayList<>();
		}
		List<IssueVO> vos = new ArrayList<>();
		for (Issue issue : issues) {
			vos.add(convertFor(issue));
		}
		return vos;
	}

	public static IssueVO convertFor(Issue issue) {
		if (issue == null) {
			return null;
		}
		IssueVO vo = new IssueVO();
		BeanUtils.copyProperties(issue, vo);
		vo.setGameName(DictHolder.getDictItemName("game", vo.getGameCode()));
		vo.setStateName(DictHolder.getDictItemName("issueState", vo.getState()));
		return vo;
	}

}
