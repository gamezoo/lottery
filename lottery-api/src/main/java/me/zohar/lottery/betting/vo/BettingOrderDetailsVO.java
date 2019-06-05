package me.zohar.lottery.betting.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.dictconfig.DictHolder;

/**
 * 订单详情
 * 
 * @author zohar
 * @date 2019年1月19日
 *
 */
@Data
public class BettingOrderDetailsVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 投注时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date bettingTime;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 期号
	 */
	private Long issueNum;

	/**
	 * 全部开奖号码,以逗号分隔
	 */
	private String lotteryNum;

	/**
	 * 投注底数金额
	 */
	private Double baseAmount;

	/**
	 * 倍数
	 */
	private Double multiple;

	/**
	 * 总注数
	 */
	private Long totalBettingCount;

	/**
	 * 总投注金额
	 */
	private Double totalBettingAmount;

	/**
	 * 总中奖金额
	 */
	private Double totalWinningAmount;

	/**
	 * 总盈亏
	 */
	private Double totalProfitAndLoss;

	/**
	 * 追号标识
	 */
	private Boolean trackingNumberFlag;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 返点金额
	 */
	private Double rebateAmount;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 状态名称
	 */
	private String stateName;

	/**
	 * 投注人用户账号id
	 */
	private String userAccountId;

	/**
	 * 投注人用户名
	 */
	private String userName;

	private Boolean cancelOrderFlag = false;

	/**
	 * 投注记录集合
	 */
	private List<BettingRecordVO> bettingRecords = new ArrayList<>();

	public static BettingOrderDetailsVO convertFor(BettingOrder bettingOrder) {
		if (bettingOrder == null) {
			return null;
		}
		BettingOrderDetailsVO vo = new BettingOrderDetailsVO();
		BeanUtils.copyProperties(bettingOrder, vo);
		vo.setGameName(DictHolder.getDictItemName("game", vo.getGameCode()));
		vo.setStateName(DictHolder.getDictItemName("bettingOrderState", vo.getState()));
		if (bettingOrder.getUserAccount() != null) {
			vo.setUserName(bettingOrder.getUserAccount().getUserName());
		}
		if (Constant.投注订单状态_未开奖.equals(bettingOrder.getState())) {
			if (bettingOrder.getIssue() != null) {
				if (new Date().getTime() < bettingOrder.getIssue().getEndTime().getTime()) {
					vo.setCancelOrderFlag(true);
				}
			}
		}
		vo.setBettingRecords(BettingRecordVO.convertFor(bettingOrder.getBettingRecords()));
		return vo;
	}

}
