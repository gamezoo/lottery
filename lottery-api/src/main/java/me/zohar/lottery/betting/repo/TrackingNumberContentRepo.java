package me.zohar.lottery.betting.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.betting.domain.TrackingNumberContent;

public interface TrackingNumberContentRepo
		extends JpaRepository<TrackingNumberContent, String>, JpaSpecificationExecutor<TrackingNumberContent> {

}
