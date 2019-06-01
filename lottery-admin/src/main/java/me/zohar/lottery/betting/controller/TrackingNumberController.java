package me.zohar.lottery.betting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.betting.param.TrackingNumberSituationQueryCondParam;
import me.zohar.lottery.betting.service.TrackingNumberService;
import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;

@Controller
@RequestMapping("/trackingNumber")
public class TrackingNumberController {

	@Autowired
	private TrackingNumberService trackingNumberService;

	@GetMapping("/findTrackingNumberSituationByPage")
	@ResponseBody
	public Result findTrackingNumberSituationByPage(TrackingNumberSituationQueryCondParam param) {
		return Result.success().setData(trackingNumberService.findTrackingNumberSituationByPage(param));
	}

	@GetMapping("/findTrackingNumberOrderDetails")
	@ResponseBody
	public Result findTrackingNumberOrderDetails(String id) {
		return Result.success().setData(trackingNumberService.findTrackingNumberOrderDetails(id));
	}
	
	@GetMapping("/cancelOrder")
	@ResponseBody
	public Result cancelOrder(String orderId) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		trackingNumberService.cancelOrder(orderId, user.getUserAccountId());
		return Result.success();
	}

}
