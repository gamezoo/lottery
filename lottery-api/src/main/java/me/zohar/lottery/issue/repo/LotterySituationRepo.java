package me.zohar.lottery.issue.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.issue.domain.LotterySituation;

public interface LotterySituationRepo
		extends JpaRepository<LotterySituation, String>, JpaSpecificationExecutor<LotterySituation> {

}
