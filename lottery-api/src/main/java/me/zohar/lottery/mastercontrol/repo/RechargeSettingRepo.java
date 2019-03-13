package me.zohar.lottery.mastercontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.mastercontrol.domain.RechargeSetting;

public interface RechargeSettingRepo
		extends JpaRepository<RechargeSetting, String>, JpaSpecificationExecutor<RechargeSetting> {
	
	RechargeSetting findTopByOrderByLatelyUpdateTime();

}
