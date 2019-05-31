package me.zohar.lottery.information.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.information.param.LotteryInformationQueryCondParam;
import me.zohar.lottery.information.service.LotteryInformationService;

@Controller
@RequestMapping("/lotteryInformation")
public class LotteryInformationController {

	@Autowired
	private LotteryInformationService lotteryInformationService;

	@GetMapping("/findInformationById")
	@ResponseBody
	public Result findInformationById(String id) {
		return Result.success().setData(lotteryInformationService.findInformationById(id));
	}

	@GetMapping("/findTop13Information")
	@ResponseBody
	public Result findTop13Information() {
		return Result.success().setData(lotteryInformationService.findTop13Information());
	}

	@GetMapping("/findLotteryInformationByPage")
	@ResponseBody
	public Result findLotteryInformationByPage(LotteryInformationQueryCondParam param) {
		return Result.success().setData(lotteryInformationService.findLotteryInformationByPage(param));
	}

}
