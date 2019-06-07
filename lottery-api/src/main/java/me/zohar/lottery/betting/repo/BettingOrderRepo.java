package me.zohar.lottery.betting.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.betting.domain.BettingOrder;

public interface BettingOrderRepo extends JpaRepository<BettingOrder, String>, JpaSpecificationExecutor<BettingOrder> {

	/**
	 * 根据游戏代码,期号,状态获取投注订单
	 * 
	 * @param gameCode
	 * @param issue
	 * @param state
	 * @return
	 */
	List<BettingOrder> findByGameCodeAndIssueNumAndState(String gameCode, Long issueNum, String state);

	List<BettingOrder> findTop50ByBettingTimeGreaterThanAndStateOrderByTotalWinningAmountDesc(Date bettingTime,
			String state);

	List<BettingOrder> findTop50ByStateOrderByTotalWinningAmountDesc(String state);
	
	List<BettingOrder> findByGameCodeAndIssueNum(String gameCode, Long issueNum);

}
