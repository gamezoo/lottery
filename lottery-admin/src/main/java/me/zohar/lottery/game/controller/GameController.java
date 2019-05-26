package me.zohar.lottery.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.game.param.GameCategoryParam;
import me.zohar.lottery.game.param.GameParam;
import me.zohar.lottery.game.param.GamePlayParam;
import me.zohar.lottery.game.service.GameService;

@Controller
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameService gameService;

	@GetMapping("/dictSync")
	@ResponseBody
	public Result dictSync(Boolean syncGameDict, Boolean syncGamePlayDict) {
		gameService.dictSync(syncGameDict, syncGamePlayDict);
		return Result.success();
	}

	@GetMapping("/findGameByGameCategoryId")
	@ResponseBody
	public Result findGameByGameCategoryId(String gameCategoryId) {
		return Result.success().setData(gameService.findGameByGameCategoryId(gameCategoryId));
	}

	@GetMapping("/delGameById")
	@ResponseBody
	public Result delGameById(String id) {
		gameService.delGameById(id);
		return Result.success();
	}

	@PostMapping("/addOrUpdateGame")
	@ResponseBody
	public Result addOrUpdateGame(@RequestBody GameParam gameParam) {
		gameService.addOrUpdateGame(gameParam);
		return Result.success();
	}

	@GetMapping("/findGameById")
	@ResponseBody
	public Result findGameById(String id) {
		return Result.success().setData(gameService.findGameById(id));
	}

	@GetMapping("/findGamePlayByGameCode")
	@ResponseBody
	public Result findGamePlayByGameCode(String gameCode) {
		return Result.success().setData(gameService.findGamePlayByGameCode(gameCode));
	}

	@GetMapping("/findGamePlayDetailsById")
	@ResponseBody
	public Result findGamePlayDetailsById(String id) {
		return Result.success().setData(gameService.findGamePlayDetailsById(id));
	}

	@GetMapping("/updateGamePlayState")
	@ResponseBody
	public Result updateGamePlayState(String id, String state) {
		gameService.updateGamePlayState(id, state);
		return Result.success();
	}

	@PostMapping("/addOrUpdateGamePlay")
	@ResponseBody
	public Result addOrUpdateGamePlay(@RequestBody GamePlayParam gamePlayParam) {
		gameService.addOrUpdateGamePlay(gamePlayParam);
		return Result.success();
	}

	@GetMapping("/delGamePlayById")
	@ResponseBody
	public Result delGamePlayById(String id) {
		gameService.delGamePlayById(id);
		return Result.success();
	}

	@GetMapping("/findAllGameCategory")
	@ResponseBody
	public Result findAllGameCategory() {
		return Result.success().setData(gameService.findAllGameCategory());
	}

	@PostMapping("/addOrUpdateGameCategory")
	@ResponseBody
	public Result addOrUpdateGameCategory(@RequestBody GameCategoryParam param) {
		gameService.addOrUpdateGameCategory(param);
		return Result.success();
	}

	@GetMapping("/delGameCategoryById")
	@ResponseBody
	public Result delGameCategoryById(String id) {
		gameService.delGameCategoryById(id);
		return Result.success();
	}

	@GetMapping("/findGameCategoryById")
	@ResponseBody
	public Result findGameCategoryById(String id) {
		return Result.success().setData(gameService.findGameCategoryById(id));
	}

}
