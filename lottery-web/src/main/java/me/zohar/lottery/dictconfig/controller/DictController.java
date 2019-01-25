package me.zohar.lottery.dictconfig.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.dictconfig.DictHolder;

@Controller
@RequestMapping("/dict")
public class DictController {

	@GetMapping("/findDictItemInCache")
	@ResponseBody
	public Result findDictItemInCache(String dictTypeCode) {
		return Result.success().setData(DictHolder.findDictItem(dictTypeCode));
	}

}
