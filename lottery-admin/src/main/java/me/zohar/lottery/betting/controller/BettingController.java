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
import me.zohar.lottery.betting.param.ChangeOrderParam;
import me.zohar.lottery.betting.service.BettingService;
import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;

@Controller
@RequestMapping("/betting")
public class BettingController {

	@Autowired
	private BettingService bettingService;

	/**
	 * 获取投注信息详情
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/findBettingOrderDetails")
	@ResponseBody
	public Result findBettingOrderDetails(String id) {
		return Result.success().setData(bettingService.findBettingOrderDetails(id));
	}

	/**
	 * 分页获取投注订单信息
	 * 
	 * @param param
	 * @return
	 */
	@GetMapping("/findBettingOrderInfoByPage")
	@ResponseBody
	public Result findBettingOrderInfoByPage(BettingOrderQueryCondParam param) {
		return Result.success().setData(bettingService.findBettingOrderInfoByPage(param));
	}

	/**
	 * 改单
	 * 
	 * @return
	 */
	@PostMapping("/changeOrder")
	@ResponseBody
	public Result changeOrder(@RequestBody List<ChangeOrderParam> changeOrderParams) {
		bettingService.changeOrder(changeOrderParams);
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

}
