package me.zohar.lottery.issue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.issue.service.IssueService;

@Controller
@RequestMapping("/issue")
public class IssueController {

	@Autowired
	private IssueService issueService;

	@GetMapping("/findLatelyThe50TimeIssue")
	@ResponseBody
	public Result findLatelyThe50TimeIssue(String gameCode) {
		return Result.success().setData(issueService.findLatelyThe50TimeIssue(gameCode));
	}

	@GetMapping("/getCurrentIssue")
	@ResponseBody
	public Result getCurrentIssue(String gameCode) {
		return Result.success().setData(issueService.getCurrentIssue(gameCode));
	}

	@GetMapping("/getNextIssue")
	@ResponseBody
	public Result getNextIssue(String gameCode) {
		return Result.success().setData(issueService.getNextIssue(gameCode));
	}

	@GetMapping("/getLatelyIssue")
	@ResponseBody
	public Result getLatelyIssue(String gameCode) {
		return Result.success().setData(issueService.getLatelyIssue(gameCode));
	}

	@GetMapping("/findTodayTrackingNumberIssue")
	@ResponseBody
	public Result findTodayTrackingNumberIssue(String gameCode) {
		return Result.success().setData(issueService.findTodayTrackingNumberIssue(gameCode));
	}

}
