package me.zohar.lottery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	/**
	 * 登录页面
	 * 
	 * @return
	 */
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
	 * 注册页面
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
	 * 个人中心页面
	 * 
	 * @return
	 */
	@GetMapping("/personal-center")
	public String personalCenter() {
		return "personal-center";
	}

	/**
	 * 投注记录页面
	 * 
	 * @return
	 */
	@GetMapping("/betting-record")
	public String bettingRecord() {
		return "betting-record";
	}

	/**
	 * 充值页面
	 * 
	 * @return
	 */
	@GetMapping("/recharge")
	public String recharge() {
		return "recharge";
	}

	/**
	 * 支付成功页面
	 * 
	 * @return
	 */
	@GetMapping("/pay-success")
	public String paySuccess() {
		return "pay-success";
	}

}
