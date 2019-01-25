package me.zohar.lottery.betting.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.betting.domain.BettingRecord;

public interface BettingRecordRepo
		extends JpaRepository<BettingRecord, String>, JpaSpecificationExecutor<BettingRecord> {

	/**
	 * 获取当前用户在指定游戏,大于指定时间的最新5次投注记录
	 * 
	 * @param userAccountId
	 * @param gameCategory
	 * @param bettingTime
	 * @return
	 */
	List<BettingRecord> findTop5ByBettingOrder_UserAccountIdAndBettingOrder_GameCodeAndBettingOrder_BettingTimeGreaterThanEqualOrderByBettingOrder_BettingTimeDesc(
			String userAccountId, String gameCode, Date bettingTime);

	List<BettingRecord> findByBettingOrderId(String bettingOrderId);

}
