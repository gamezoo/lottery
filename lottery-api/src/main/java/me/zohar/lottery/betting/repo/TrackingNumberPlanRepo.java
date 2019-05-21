package me.zohar.lottery.betting.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.betting.domain.TrackingNumberPlan;

public interface TrackingNumberPlanRepo
		extends JpaRepository<TrackingNumberPlan, String>, JpaSpecificationExecutor<TrackingNumberPlan> {

	TrackingNumberPlan findByBettingOrderId(String bettingOrderId);
	
	
	List<TrackingNumberPlan> findByTrackingNumberOrderId(String trackingNumberOrderId);

}
