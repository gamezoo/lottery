package me.zohar.lottery.betting.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.betting.domain.BettingRebate;

public interface BettingRebateRepo
		extends JpaRepository<BettingRebate, String>, JpaSpecificationExecutor<BettingRebate> {

	List<BettingRebate> findBySettlementTimeIsNull();
	
	List<BettingRebate> findByBettingOrderId(String bettingOrderId);

}
