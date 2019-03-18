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

}
