package me.zohar.lottery.issue.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.issue.domain.LotterySituation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotterySituationVO {

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
	 * 结算时间:yyyy-MM-dd HH:mm:ss
	 */
	private String settlementTime;

	/**
	 * 状态
	 */
	private String state;

	private String stateName;

	/**
	 * 总投注金额
	 */
	private Double totalBettingAmount;

	/**
	 * 总中奖金额
	 */
	private Double totalWinningAmount;

	public static List<LotterySituationVO> convertFor(List<LotterySituation> lotterySituations) {
		if (CollectionUtil.isEmpty(lotterySituations)) {
			return new ArrayList<>();
		}
		List<LotterySituationVO> vos = new ArrayList<>();
		for (LotterySituation lotterySituation : lotterySituations) {
			vos.add(convertFor(lotterySituation));
		}
		return vos;
	}

	public static LotterySituationVO convertFor(LotterySituation lotterySituation) {
		if (lotterySituation == null) {
			return null;
		}
		LotterySituationVO vo = new LotterySituationVO();
		BeanUtils.copyProperties(lotterySituation, vo);
		vo.setGameName(DictHolder.getDictItemName("game", vo.getGameCode()));
		vo.setLotteryDate(DateUtil.format(lotterySituation.getLotteryDate(), DatePattern.NORM_DATE_PATTERN));
		vo.setLotteryTime(DateUtil.format(lotterySituation.getLotteryTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setStartTime(DateUtil.format(lotterySituation.getStartTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setEndTime(DateUtil.format(lotterySituation.getEndTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setSyncTime(DateUtil.format(lotterySituation.getSyncTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setSettlementTime(DateUtil.format(lotterySituation.getSettlementTime(), DatePattern.NORM_DATETIME_PATTERN));
		vo.setStateName(DictHolder.getDictItemName("issueState", vo.getState()));
		return vo;
	}

}
