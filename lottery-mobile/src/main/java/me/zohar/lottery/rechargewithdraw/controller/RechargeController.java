package me.zohar.lottery.rechargewithdraw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;
import me.zohar.lottery.rechargewithdraw.param.MuspayCallbackParam;
import me.zohar.lottery.rechargewithdraw.param.RechargeOrderParam;
import me.zohar.lottery.rechargewithdraw.service.RechargeService;

/**
 * 
 * @author zohar
 * @date 2019年1月21日
 *
 */
@Controller
@RequestMapping("/recharge")
public class RechargeController {

	@Autowired
	private RechargeService rechargeService;

	@RequestMapping("/muspayCallback")
	@ResponseBody
	public String muspayCallback(@RequestBody MuspayCallbackParam param) throws IOException {
		rechargeService.checkOrderWithMuspay(param);
		return Result.success().getMsg();
	}

	@PostMapping("/generateRechargeOrder")
	@ResponseBody
	public Result generateRechargeOrder(RechargeOrderParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(rechargeService.generateRechargeOrder(param));
	}

}
