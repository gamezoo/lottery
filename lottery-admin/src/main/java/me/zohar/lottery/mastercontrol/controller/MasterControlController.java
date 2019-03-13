package me.zohar.lottery.mastercontrol.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.mastercontrol.service.MasterControlService;

/**
 * 总控
 * 
 * @author zohar
 * @date 2019年3月9日
 *
 */
@Controller
@RequestMapping("/masterControl")
public class MasterControlController {

	@Autowired
	private MasterControlService service;

	@GetMapping("/getInviteRegisterSetting")
	@ResponseBody
	public Result getInviteRegisterSetting() {
		return Result.success().setData(service.getInviteRegisterSetting());
	}

	@PostMapping("/updateInviteRegisterSetting")
	@ResponseBody
	public Result updateInviteRegisterSetting(Integer effectiveDuration, Boolean enabled) {
		service.updateInviteRegisterSetting(effectiveDuration, enabled);
		return Result.success();
	}

	@GetMapping("/getRegisterAmountSetting")
	@ResponseBody
	public Result getRegisterAmountSetting() {
		return Result.success().setData(service.getRegisterAmountSetting());
	}

	@PostMapping("/updateRegisterAmountSetting")
	@ResponseBody
	public Result updateRegisterAmountSetting(Double registerAmount, Boolean enabled) {
		service.updateRegisterAmountSetting(registerAmount, enabled);
		return Result.success();
	}

	@GetMapping("/getRechargeSetting")
	@ResponseBody
	public Result getRechargeSetting() {
		return Result.success().setData(service.getRechargeSetting());
	}

	@PostMapping("/updateRechargeSetting")
	@ResponseBody
	public Result updateRechargeSetting(Integer orderEffectiveDuration, Integer returnWaterRate,
			Boolean returnWaterRateEnabled) {
		service.updateRechargeSetting(orderEffectiveDuration, returnWaterRate, returnWaterRateEnabled);
		return Result.success();
	}

	@PostMapping("/refreshCache")
	@ResponseBody
	public Result refreshCache(@RequestBody List<String> cacheItems) {
		service.refreshCache(cacheItems);
		return Result.success();
	}

}
