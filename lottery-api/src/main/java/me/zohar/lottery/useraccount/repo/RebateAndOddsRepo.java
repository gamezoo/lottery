package me.zohar.lottery.useraccount.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.useraccount.domain.RebateAndOdds;

public interface RebateAndOddsRepo extends JpaRepository<RebateAndOdds, String>, JpaSpecificationExecutor<RebateAndOdds> {

	RebateAndOdds findTopByRebate(Double rebate);

}
