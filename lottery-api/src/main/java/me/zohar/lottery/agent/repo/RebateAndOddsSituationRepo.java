package me.zohar.lottery.agent.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.agent.domain.RebateAndOddsSituation;

public interface RebateAndOddsSituationRepo
		extends JpaRepository<RebateAndOddsSituation, String>, JpaSpecificationExecutor<RebateAndOddsSituation> {

}
