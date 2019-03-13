package me.zohar.lottery.mastercontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.mastercontrol.domain.RegisterAmountSetting;

public interface RegisterAmountSettingRepo
		extends JpaRepository<RegisterAmountSetting, String>, JpaSpecificationExecutor<RegisterAmountSetting> {

	RegisterAmountSetting findTopByOrderByEnabled();

}
