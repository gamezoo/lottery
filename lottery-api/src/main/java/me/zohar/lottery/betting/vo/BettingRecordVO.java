package me.zohar.lottery.betting.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.betting.domain.BettingRecord;
import me.zohar.lottery.dictconfig.DictHolder;

/**
 * 投注记录
 * 
 * @author zohar
 * @date 2019年1月19日
 *
 */
@Data
public class BettingRecordVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 所选号码
	 */
	private String selectedNo;

	/**
	 * 注数
	 */
	private Long bettingCount;

	/**
	 * 游戏玩法代码
	 */
	private String gamePlayCode;

	/**
	 * 游戏玩法名称
	 */
	private String gamePlayName;

	/**
	 * 投注金额
	 */
	private Double bettingAmount;

	/**
	 * 中奖金额
	 */
	private Double winningAmount;

	/**
	 * 投注订单id
	 */
	private String bettingOrderId;

	/**
	 * 期号
	 */
	private Long issueNum;

	/**
	 * 状态名称
	 */
	private String stateName;

	public static List<BettingRecordVO> convertFor(Collection<BettingRecord> bettingRecords) {
		if (CollectionUtil.isEmpty(bettingRecords)) {
			return new ArrayList<>();
		}
		List<BettingRecordVO> vos = new ArrayList<>();
		for (BettingRecord bettingRecord : bettingRecords) {
			vos.add(convertFor(bettingRecord));
		}
		return vos;
	}

	public static BettingRecordVO convertFor(BettingRecord bettingRecord) {
		if (bettingRecord == null) {
			return null;
		}
		BettingRecordVO vo = new BettingRecordVO();
		BeanUtils.copyProperties(bettingRecord, vo);
		vo.setGamePlayName(DictHolder.getDictItemName("gamePlay",
				bettingRecord.getBettingOrder().getGameCode() + "_" + bettingRecord.getGamePlayCode()));
		vo.setIssueNum(bettingRecord.getBettingOrder().getIssueNum());
		vo.setStateName(DictHolder.getDictItemName("bettingOrderState", bettingRecord.getBettingOrder().getState()));
		return vo;
	}

}
