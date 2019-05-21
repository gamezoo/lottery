package me.zohar.lottery.betting.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.betting.domain.TrackingNumberSituation;

public interface TrackingNumberSituationRepo
		extends JpaRepository<TrackingNumberSituation, String>, JpaSpecificationExecutor<TrackingNumberSituation> {

}
