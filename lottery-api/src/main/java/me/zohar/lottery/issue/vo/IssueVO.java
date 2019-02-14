package me.zohar.lottery.issue.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zohar.lottery.common.utils.IdUtils;
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
	 * 开奖日期:yyyy-MM-dd
	 */
	private String lotteryDate;

	/**
	 * 开奖时间:yyyy-MM-dd HH:mm:ss
	 */
	private String lotteryTime;

	/**
	 * 开始时间:yyyy-MM-dd HH:mm:ss
	 */
	private String startTime;

	/**
	 * 结束时间:yyyy-MM-dd HH:mm:ss
	 */
	private String endTime;

	/**
	 * 全部开奖号码,以逗号分隔
	 */
	private String lotteryNum;
	
	/**
	 * 同步时间:yyyy-MM-dd HH:mm:ss
	 */
	private String syncTime;

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
		vo.setLotteryDate(DateUtil.format(issue.getLotteryDate(), DatePattern.NORM_DATE_PATTERN));
		vo.setLotteryTime(DateUtil.format(issue.getLotteryTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setStartTime(DateUtil.format(issue.getStartTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setEndTime(DateUtil.format(issue.getEndTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setSyncTime(DateUtil.format(issue.getSyncTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setStateName(DictHolder.getDictItemName("issueState", vo.getState()));
		return vo;
	}

	public Issue convertToPo() {
		Issue po = new Issue();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setLotteryDate(DateUtil.beginOfDay(DateUtil.parse(this.getLotteryDate(), DatePattern.NORM_DATE_PATTERN)));
		po.setSyncTime(new Date());
		return po;
	}

}
