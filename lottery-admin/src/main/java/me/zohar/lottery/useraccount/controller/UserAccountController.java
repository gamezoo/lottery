package me.zohar.lottery.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.useraccount.param.AccountChangeLogQueryCondParam;
import me.zohar.lottery.useraccount.param.BindBankInfoParam;
import me.zohar.lottery.useraccount.param.UserAccountEditParam;
import me.zohar.lottery.useraccount.param.UserAccountQueryCondParam;
import me.zohar.lottery.useraccount.service.UserAccountService;

@Controller
@RequestMapping("/userAccount")
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;
	
	@GetMapping("/findAccountChangeLogByPage")
	@ResponseBody
	public Result findAccountChangeLogByPage(AccountChangeLogQueryCondParam param) {
		return Result.success().setData(userAccountService.findAccountChangeLogByPage(param));
	}
	
	@GetMapping("/findUserAccountDetailsInfoById")
	@ResponseBody
	public Result findUserAccountDetailsInfoById(String userAccountId) {
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoById(userAccountId));
	}

	@GetMapping("/findUserAccountDetailsInfoByPage")
	@ResponseBody
	public Result findUserAccountDetailsInfoByPage(UserAccountQueryCondParam param) {
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoByPage(param));
	}

	@PostMapping("/bindBankInfo")
	@ResponseBody
	public Result bindBankInfo(BindBankInfoParam param) {
		userAccountService.bindBankInfo(param);
		return Result.success();
	}

	@GetMapping("/getBankInfo")
	@ResponseBody
	public Result getBankInfo(String userAccountId) {
		return Result.success().setData(userAccountService.getBankInfo(userAccountId));
	}

	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(String userAccountId, String newLoginPwd) {
		userAccountService.modifyLoginPwd(userAccountId, newLoginPwd);
		return Result.success();
	}

	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(String userAccountId, String newMoneyPwd) {
		userAccountService.modifyMoneyPwd(userAccountId, newMoneyPwd);
		return Result.success();
	}
	
	@PostMapping("/updateUserAccount")
	@ResponseBody
	public Result updateUserAccount(UserAccountEditParam param) {
		userAccountService.updateUserAccount(param);
		return Result.success();
	}
	
	

}
