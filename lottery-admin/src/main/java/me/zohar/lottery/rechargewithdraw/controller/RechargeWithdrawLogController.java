package me.zohar.lottery.rechargewithdraw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;
import me.zohar.lottery.rechargewithdraw.param.RechargeWithdrawLogQueryCondParam;
import me.zohar.lottery.rechargewithdraw.service.RechargeWithdrawLogService;

@Controller
@RequestMapping("/rechargeWithdrawLog")
public class RechargeWithdrawLogController {

	@Autowired
	private RechargeWithdrawLogService rechargeWithdrawLogService;

	@GetMapping("/findMyRechargeWithdrawLogByPage")
	@ResponseBody
	public Result findMyRechargeWithdrawLogByPage(RechargeWithdrawLogQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(rechargeWithdrawLogService.findMyRechargeWithdrawLogByPage(param));
	}

}
