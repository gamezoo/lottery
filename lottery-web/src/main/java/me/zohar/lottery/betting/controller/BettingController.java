package me.zohar.lottery.betting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.betting.param.BettingOrderQueryCondParam;
import me.zohar.lottery.betting.param.LowerLevelBettingOrderQueryCondParam;
import me.zohar.lottery.betting.param.PlaceOrderParam;
import me.zohar.lottery.betting.service.BettingService;
import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;

@Controller
@RequestMapping("/betting")
public class BettingController {

	@Autowired
	private BettingService bettingService;

	@GetMapping("/findTop50WinningRank")
	@ResponseBody
	public Result findTop50WinningRank() {
		return Result.success().setData(bettingService.findTop50WinningRank());
	}

	/**
	 * 获取我或下级账号投注信息详情
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/findMyOrLowerLevelBettingOrderDetails")
	@ResponseBody
	public Result findMyOrLowerLevelBettingOrderDetails(String id) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(bettingService.findMyOrLowerLevelBettingOrderDetails(id, user.getUserAccountId()));
	}

	/**
	 * 分页获取我的投注订单信息
	 * 
	 * @param param
	 * @return
	 */
	@GetMapping("/findMyBettingOrderInfoByPage")
	@ResponseBody
	public Result findMyBettingOrderInfoByPage(BettingOrderQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(bettingService.findMyBettingOrderInfoByPage(param));
	}

	@GetMapping("/findTodayLatestThe5TimeBettingRecord")
	@ResponseBody
	public Result findTodayLatestThe5TimeBettingRecord(String gameCode) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success()
				.setData(bettingService.findTodayLatestThe5TimeBettingRecord(user.getUserAccountId(), gameCode));
	}

	/**
	 * 下单
	 * 
	 * @return
	 */
	@PostMapping("/placeOrder")
	@ResponseBody
	public Result placeOrder(@RequestBody PlaceOrderParam placeOrderParam) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		bettingService.placeOrder(placeOrderParam, user.getUserAccountId());
		return Result.success();
	}

	@GetMapping("/cancelOrder")
	@ResponseBody
	public Result cancelOrder(String orderId) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		bettingService.cancelOrder(orderId, user.getUserAccountId());
		return Result.success();
	}

	@PostMapping("/batchCancelOrder")
	@ResponseBody
	public Result batchCancelOrder(@RequestBody List<String> orderIds) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		bettingService.batchCancelOrder(orderIds, user.getUserAccountId());
		return Result.success();
	}

	@GetMapping("/findLowerLevelBettingOrderInfoByPage")
	@ResponseBody
	public Result findLowerLevelBettingOrderInfoByPage(LowerLevelBettingOrderQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setCurrentAccountId(user.getUserAccountId());
		return Result.success().setData(bettingService.findLowerLevelBettingOrderInfoByPage(param));
	}

}
