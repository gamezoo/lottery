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
	 * 游戏管理页面
	 * 
	 * @return
	 */
	@GetMapping("/game-manage")
	public String gameManage() {
		return "game-manage";
	}

}
