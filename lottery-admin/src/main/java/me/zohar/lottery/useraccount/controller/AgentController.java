package me.zohar.lottery.useraccount.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.agent.param.AddOrUpdateRebateAndOddsParam;
import me.zohar.lottery.agent.service.AgentService;
import me.zohar.lottery.common.param.PageParam;
import me.zohar.lottery.common.vo.Result;

@Controller
@RequestMapping("/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

	@GetMapping("/findAllRebateAndOdds")
	@ResponseBody
	public Result findAllRebateAndOdds() {
		return Result.success().setData(agentService.findAllRebateAndOdds());
	}

	@GetMapping("/findRebateAndOddsSituationByPage")
	@ResponseBody
	public Result findRebateAndOddsSituationByPage(PageParam param) {
		return Result.success().setData(agentService.findRebateAndOddsSituationByPage(param));
	}

	@PostMapping("/resetRebateAndOdds")
	@ResponseBody
	public Result resetRebateAndOdds(@RequestBody List<AddOrUpdateRebateAndOddsParam> params) {
		agentService.resetRebateAndOdds(params);
		return Result.success();
	}

	@GetMapping("/findRebateAndOdds")
	@ResponseBody
	public Result findRebateAndOdds(Double rebate, Double odds) {
		return Result.success().setData(agentService.findRebateAndOdds(rebate, odds));
	}

	@PostMapping("/addOrUpdateRebateAndOdds")
	@ResponseBody
	public Result addOrUpdateRebateAndOdds(@RequestBody AddOrUpdateRebateAndOddsParam param) {
		agentService.addOrUpdateRebateAndOdds(param);
		return Result.success();
	}

	@GetMapping("/delRebateAndOdds")
	@ResponseBody
	public Result delRebateAndOdds(Double rebate, Double odds) {
		agentService.delRebateAndOdds(rebate, odds);
		return Result.success();
	}
}
