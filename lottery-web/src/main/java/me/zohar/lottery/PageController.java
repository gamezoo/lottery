package me.zohar.lottery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	/**
	 * 首页
	 * 
	 * @return
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	/**
	 * 注册
	 * 
	 * @return
	 */
	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

	/**
	 * 重庆时时彩
	 * 
	 * @return
	 */
	@GetMapping("/cqssc")
	public String cqssc() {
		return "cqssc";
	}

	/**
	 * 新疆时时彩
	 * 
	 * @return
	 */
	@GetMapping("/xjssc")
	public String xjssc() {
		return "xjssc";
	}

	/**
	 * 江西11选5
	 * 
	 * @return
	 */
	@GetMapping("/jx11x5")
	public String jx11x5() {
		return "jx11x5";
	}

	/**
	 * 个人中心
	 * 
	 * @return
	 */
	@GetMapping("/personal-center")
	public String personalCenter() {
		return "personal-center";
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
	 * 充值
	 * 
	 * @return
	 */
	@GetMapping("/recharge")
	public String recharge() {
		return "recharge";
	}

	/**
	 * 支付成功
	 * 
	 * @return
	 */
	@GetMapping("/pay-success")
	public String paySuccess() {
		return "pay-success";
	}

	/**
	 * 提现
	 * 
	 * @return
	 */
	@GetMapping("/withdraw")
	public String withdraw() {
		return "withdraw";
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

}
