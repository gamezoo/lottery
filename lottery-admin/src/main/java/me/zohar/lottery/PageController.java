package me.zohar.lottery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * 登录页面
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	/**
	 * 游戏管理页面
	 * 
	 * @return
	 */
	@GetMapping("/game-manage")
	public String gameManage() {
		return "game-manage";
	}

	/**
	 * 开奖情况
	 * 
	 * @return
	 */
	@GetMapping("/lottery-situation")
	public String lotterySituation() {
		return "lottery-situation";
	}

	/**
	 * 投注记录
	 * 
	 * @return
	 */
	@GetMapping("/betting-record")
	public String bettingRecord() {
		return "betting-record";
	}

	/**
	 * 账号管理
	 * 
	 * @return
	 */
	@GetMapping("/account-manage")
	public String accountManage() {
		return "account-manage";
	}

	/**
	 * 帐变日志
	 * 
	 * @return
	 */
	@GetMapping("/account-change-log")
	public String accountChangeLog() {
		return "account-change-log";
	}

	/**
	 * 充值订单
	 * 
	 * @return
	 */
	@GetMapping("/recharge-order")
	public String rechargeOrder() {
		return "recharge-order";
	}

	/**
	 * 提现记录
	 * 
	 * @return
	 */
	@GetMapping("/withdraw-record")
	public String withdrawRecord() {
		return "withdraw-record";
	}

	/**
	 * 配置项管理
	 * 
	 * @return
	 */
	@GetMapping("/config-manage")
	public String configManage() {
		return "config-manage";
	}

	/**
	 * 字典管理
	 * 
	 * @return
	 */
	@GetMapping("/dict-manage")
	public String dictManage() {
		return "dict-manage";
	}

	/**
	 * 总控室
	 * 
	 * @return
	 */
	@GetMapping("/master-control-room")
	public String masterControlRoom() {
		return "master-control-room";
	}

	/**
	 * 系统公告
	 * 
	 * @return
	 */
	@GetMapping("/system-notice")
	public String systemNotice() {
		return "system-notice";
	}

	/**
	 * 彩票资讯
	 * 
	 * @return
	 */
	@GetMapping("/lottery-information")
	public String lotteryInformation() {
		return "lottery-information";
	}

	/**
	 * 登录日志
	 * 
	 * @return
	 */
	@GetMapping("/login-log")
	public String loginLog() {
		return "login-log";
	}

	/**
	 * 追号记录
	 * 
	 * @return
	 */
	@GetMapping("/tracking-number-record")
	public String trackingNumberRecord() {
		return "tracking-number-record";
	}

	/**
	 * 返点赔率
	 * 
	 * @return
	 */
	@GetMapping("/rebate-and-odds")
	public String rebateAndOdds() {
		return "rebate-and-odds";
	}

}
