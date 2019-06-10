package me.zohar.lottery.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.agent.param.AgentOpenAnAccountParam;
import me.zohar.lottery.agent.param.GenerateInviteCodeParam;
import me.zohar.lottery.agent.service.AgentService;
import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;
import me.zohar.lottery.statisticalanalysis.param.AccountProfitAndLossQueryCondParam;
import me.zohar.lottery.statisticalanalysis.service.StatisticalAnalysisService;
import me.zohar.lottery.useraccount.param.LowerLevelAccountQueryCondParam;
import me.zohar.lottery.useraccount.service.UserAccountService;

@Controller
@RequestMapping("/agent")
public class AgentController {

	@Autowired
	private AgentService agentService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private StatisticalAnalysisService statisticalAnalysisService;

	@GetMapping("/findAllRebateAndOdds")
	@ResponseBody
	public Result findAllRebateAndOdds() {
		return Result.success().setData(agentService.findAllRebateAndOdds());
	}

	@PostMapping("/agentOpenAnAccount")
	@ResponseBody
	public Result agentOpenAnAccount(@RequestBody AgentOpenAnAccountParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setInviterId(user.getUserAccountId());
		agentService.agentOpenAnAccount(param);
		return Result.success();
	}

	@PostMapping("/generateInviteCodeAndGetInviteRegisterLink")
	@ResponseBody
	public Result generateInviteCodeAndGetInviteRegisterLink(@RequestBody GenerateInviteCodeParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setInviterId(user.getUserAccountId());
		String inviteCodeId = agentService.generateInviteCode(param);
		return Result.success().setData(agentService.getInviteCodeDetailsInfoById(inviteCodeId));
	}

	@GetMapping("/findLowerLevelAccountDetailsInfoByPage")
	@ResponseBody
	public Result findLowerLevelAccountDetailsInfoByPage(LowerLevelAccountQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setCurrentAccountId(user.getUserAccountId());
		return Result.success().setData(userAccountService.findLowerLevelAccountDetailsInfoByPage(param));
	}

	@GetMapping("/findAccountProfitAndLossByPage")
	@ResponseBody
	public Result findAccountProfitAndLossByPage(AccountProfitAndLossQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setCurrentAccountId(user.getUserAccountId());
		return Result.success().setData(statisticalAnalysisService.findAccountProfitAndLossByPage(param));
	}

}
