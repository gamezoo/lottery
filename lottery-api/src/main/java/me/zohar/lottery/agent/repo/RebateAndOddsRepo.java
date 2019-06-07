package me.zohar.lottery.agent.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.agent.domain.RebateAndOdds;

public interface RebateAndOddsRepo
		extends JpaRepository<RebateAndOdds, String>, JpaSpecificationExecutor<RebateAndOdds> {

	RebateAndOdds findTopByRebate(Double rebate);

	RebateAndOdds findTopByRebateAndOdds(Double rebate, Double odds);

}
