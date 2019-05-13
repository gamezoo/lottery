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

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	/**
	 * 游戏大厅
	 * 
	 * @return
	 */
	@GetMapping("/game-hall")
	public String gameHall() {
		return "game-hall";
	}

	/**
	 * 开奖大厅
	 * 
	 * @return
	 */
	@GetMapping("/lottery-hall")
	public String lotteryHall() {
		return "lottery-hall";
	}
	
	/**
	 * 我的主页
	 * @return
	 */
	@GetMapping("/my-home-page")
	public String myHomePage() {
		return "my-home-page";
	}
	
	/**
	 * 提现
	 * @return
	 */
	@GetMapping("/withdraw")
	public String withdraw() {
		return "withdraw";
	}

}
