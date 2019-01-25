package me.zohar.lottery.gd11x5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.gd11x5.service.Gd11X5LotteryResultService;

@Controller
@RequestMapping("/gd11x5")
public class Gd11X5LotteryResultController {

	@Autowired
	private Gd11X5LotteryResultService gd11X5LotteryResultService;

	@GetMapping("/findLatelyThe5TimeLotteryResult")
	@ResponseBody
	public Result findLatelyThe5TimeLotteryResult() {
		return Result.success().setData(gd11X5LotteryResultService.findLatelyThe5TimeLotteryResult());
	}

}
