package me.zohar.lottery.mastercontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.mastercontrol.domain.InviteRegisterSetting;

public interface InviteRegisterSettingRepo
		extends JpaRepository<InviteRegisterSetting, String>, JpaSpecificationExecutor<InviteRegisterSetting> {

	InviteRegisterSetting findTopByOrderByEnabled();
	
}
