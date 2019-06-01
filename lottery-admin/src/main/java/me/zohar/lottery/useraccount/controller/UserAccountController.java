package me.zohar.lottery.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;
import me.zohar.lottery.useraccount.param.AccountChangeLogQueryCondParam;
import me.zohar.lottery.useraccount.param.AddUserAccountParam;
import me.zohar.lottery.useraccount.param.BindBankInfoParam;
import me.zohar.lottery.useraccount.param.LoginLogQueryCondParam;
import me.zohar.lottery.useraccount.param.UserAccountEditParam;
import me.zohar.lottery.useraccount.param.UserAccountQueryCondParam;
import me.zohar.lottery.useraccount.service.LoginLogService;
import me.zohar.lottery.useraccount.service.UserAccountService;
import me.zohar.lottery.useraccount.vo.UserAccountInfoVO;

@Controller
@RequestMapping("/userAccount")
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private LoginLogService loginLogService;

	@GetMapping("/findLoginLogByPage")
	@ResponseBody
	public Result findLoginLogByPage(LoginLogQueryCondParam param) {
		return Result.success().setData(loginLogService.findLoginLogByPage(param));
	}

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

	@GetMapping("/getUserAccountInfo")
	@ResponseBody
	public Result getUserAccountInfo() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			return Result.success();
		}
		UserAccountDetails user = (UserAccountDetails) principal;
		UserAccountInfoVO userAccountInfo = userAccountService.getUserAccountInfo(user.getUserAccountId());
		return Result.success().setData(userAccountInfo);
	}

	@GetMapping("/delUserAccount")
	@ResponseBody
	public Result delUserAccount(String userAccountId) {
		userAccountService.delUserAccount(userAccountId);
		return Result.success();
	}

	@PostMapping("/addUserAccount")
	@ResponseBody
	public Result addUserAccount(AddUserAccountParam param) {
		userAccountService.addUserAccount(param);
		return Result.success();
	}

}
