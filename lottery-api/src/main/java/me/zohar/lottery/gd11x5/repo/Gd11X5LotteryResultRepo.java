package me.zohar.lottery.gd11x5.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.gd11x5.domain.Gd11X5LotteryResult;

public interface Gd11X5LotteryResultRepo
		extends JpaRepository<Gd11X5LotteryResult, String>, JpaSpecificationExecutor<Gd11X5LotteryResult> {
	
	/**
	 * 获取当前日期最新的开奖结果
	 * @return
	 */
	Gd11X5LotteryResult findTopByLotteryDateOrderByIssueDesc(Date lotteryDate);
	
	/**
	 * 获取最新的5次开奖结果
	 * @param lotteryDate
	 * @return
	 */
	List<Gd11X5LotteryResult> findTop5ByOrderByIssueDesc();

}
