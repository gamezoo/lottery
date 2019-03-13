package me.zohar.lottery.mastercontrol.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.mastercontrol.domain.InviteRegisterSetting;
import me.zohar.lottery.mastercontrol.domain.RechargeSetting;
import me.zohar.lottery.mastercontrol.domain.RegisterAmountSetting;
import me.zohar.lottery.mastercontrol.repo.InviteRegisterSettingRepo;
import me.zohar.lottery.mastercontrol.repo.RechargeSettingRepo;
import me.zohar.lottery.mastercontrol.repo.RegisterAmountSettingRepo;
import me.zohar.lottery.mastercontrol.vo.InviteRegisterSettingVO;
import me.zohar.lottery.mastercontrol.vo.RechargeSettingVO;
import me.zohar.lottery.mastercontrol.vo.RegisterAmountSettingVO;

@Validated
@Service
@Slf4j
public class MasterControlService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RegisterAmountSettingRepo registerAmountSettingRepo;

	@Autowired
	private InviteRegisterSettingRepo inviteRegisterSettingRepo;

	@Autowired
	private RechargeSettingRepo rechargeSettingRepo;

	@Transactional(readOnly = true)
	public InviteRegisterSettingVO getInviteRegisterSetting() {
		InviteRegisterSetting setting = inviteRegisterSettingRepo.findTopByOrderByEnabled();
		return InviteRegisterSettingVO.convertFor(setting);
	}

	@Transactional
	public void updateInviteRegisterSetting(
			@NotNull @DecimalMin(value = "0", inclusive = true) Integer effectiveDuration, @NotNull Boolean enabled) {
		InviteRegisterSetting setting = inviteRegisterSettingRepo.findTopByOrderByEnabled();
		if (setting == null) {
			setting = InviteRegisterSetting.build();
		}
		setting.update(effectiveDuration, enabled);
		inviteRegisterSettingRepo.save(setting);
	}

	@Transactional(readOnly = true)
	public RegisterAmountSettingVO getRegisterAmountSetting() {
		RegisterAmountSetting setting = registerAmountSettingRepo.findTopByOrderByEnabled();
		return RegisterAmountSettingVO.convertFor(setting);
	}

	/**
	 * 更新注册礼金设置
	 * 
	 * @param registerAmount
	 * @param enabled
	 */
	@Transactional
	public void updateRegisterAmountSetting(@NotNull @DecimalMin(value = "0", inclusive = true) Double registerAmount,
			@NotNull Boolean enabled) {
		RegisterAmountSetting setting = registerAmountSettingRepo.findTopByOrderByEnabled();
		if (setting == null) {
			setting = RegisterAmountSetting.build();
		}
		setting.update(registerAmount, enabled);
		registerAmountSettingRepo.save(setting);
	}

	@Transactional(readOnly = true)
	public RechargeSettingVO getRechargeSetting() {
		RechargeSetting setting = rechargeSettingRepo.findTopByOrderByLatelyUpdateTime();
		return RechargeSettingVO.convertFor(setting);
	}

	@Transactional
	public void updateRechargeSetting(
			@NotNull @DecimalMin(value = "0", inclusive = true) Integer orderEffectiveDuration,
			@NotNull @DecimalMin(value = "0", inclusive = true) Integer returnWaterRate,
			@NotNull Boolean returnWaterRateEnabled) {
		RechargeSetting setting = rechargeSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting == null) {
			setting = RechargeSetting.build();
		}
		setting.update(orderEffectiveDuration, returnWaterRate, returnWaterRateEnabled);
		rechargeSettingRepo.save(setting);
	}

	/**
	 * 刷新缓存
	 * 
	 * @param cacheItems
	 */
	public void refreshCache(@NotEmpty List<String> cacheItems) {
		List<String> deleteSuccessKeys = new ArrayList<>();
		List<String> deleteFailKeys = new ArrayList<>();
		for (String cacheItem : cacheItems) {
			Set<String> keys = redisTemplate.keys(cacheItem);
			for (String key : keys) {
				Boolean flag = redisTemplate.delete(key);
				if (flag) {
					deleteSuccessKeys.add(key);
				} else {
					deleteFailKeys.add(key);
				}
			}
		}
		if (!deleteFailKeys.isEmpty()) {
			log.warn("以下的缓存删除失败:", deleteFailKeys);
		}
	}

}
