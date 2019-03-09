package me.zohar.lottery.dictconfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.dictconfig.param.AddOrUpdateDictTypeParam;
import me.zohar.lottery.dictconfig.param.ConfigItemQueryCondParam;
import me.zohar.lottery.dictconfig.param.ConfigParam;
import me.zohar.lottery.dictconfig.param.DictTypeQueryCondParam;
import me.zohar.lottery.dictconfig.param.UpdateDictDataParam;
import me.zohar.lottery.dictconfig.service.ConfigService;
import me.zohar.lottery.dictconfig.service.DictService;

@Controller
@RequestMapping("/dictconfig")
public class DictConfigController {

	@Autowired
	private ConfigService configService;

	@Autowired
	private DictService dictService;

	@PostMapping("/updateDictData")
	@ResponseBody
	public Result updateDictData(@RequestBody UpdateDictDataParam param) {
		dictService.updateDictData(param);
		return Result.success();
	}

	@GetMapping("/findDictItemByDictTypeId")
	@ResponseBody
	public Result findDictItemByDictTypeId(String dictTypeId) {
		return Result.success().setData(dictService.findDictItemByDictTypeId(dictTypeId));
	}

	@GetMapping("/delDictTypeById")
	@ResponseBody
	public Result delDictTypeById(String id) {
		dictService.delDictTypeById(id);
		return Result.success();
	}

	@PostMapping("/addOrUpdateDictType")
	@ResponseBody
	public Result addOrUpdateDictType(@RequestBody AddOrUpdateDictTypeParam param) {
		dictService.addOrUpdateDictType(param);
		return Result.success();
	}

	@GetMapping("/findDictTypeById")
	@ResponseBody
	public Result findDictTypeById(String id) {
		return Result.success().setData(dictService.findDictTypeById(id));
	}

	@GetMapping("/findDictTypeByPage")
	@ResponseBody
	public Result findDictTypeByPage(DictTypeQueryCondParam param) {
		return Result.success().setData(dictService.findDictTypeByPage(param));
	}

	@GetMapping("/findDictItemInCache")
	@ResponseBody
	public Result findDictItemInCache(String dictTypeCode) {
		return Result.success().setData(DictHolder.findDictItem(dictTypeCode));
	}

	@GetMapping("/findConfigItemByPage")
	@ResponseBody
	public Result findConfigItemByPage(ConfigItemQueryCondParam param) {
		return Result.success().setData(configService.findConfigItemByPage(param));
	}

	@GetMapping("/findConfigItemById")
	@ResponseBody
	public Result findConfigItemById(String id) {
		return Result.success().setData(configService.findConfigItemById(id));
	}

	@PostMapping("/addOrUpdateConfig")
	@ResponseBody
	public Result addOrUpdateConfig(@RequestBody ConfigParam configParam) {
		configService.addOrUpdateConfig(configParam);
		return Result.success();
	}

	@GetMapping("/delConfigById")
	@ResponseBody
	public Result delConfigById(String id) {
		configService.delConfigById(id);
		return Result.success();
	}

}
