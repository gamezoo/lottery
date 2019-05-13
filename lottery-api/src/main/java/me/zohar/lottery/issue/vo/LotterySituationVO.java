package me.zohar.lottery.issue.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
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
	 * 结算时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date settlementTime;

	/**
	 * 状态
	 */
	private String state;

	private String stateName;
	
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
		vo.setStateName(DictHolder.getDictItemName("issueState", vo.getState()));
		return vo;
	}

}
