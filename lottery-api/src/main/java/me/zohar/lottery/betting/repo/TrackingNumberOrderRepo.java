package me.zohar.lottery.betting.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.betting.domain.TrackingNumberOrder;

public interface TrackingNumberOrderRepo
		extends JpaRepository<TrackingNumberOrder, String>, JpaSpecificationExecutor<TrackingNumberOrder> {

}
