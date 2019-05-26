package me.zohar.lottery.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.game.service.GameService;

@Controller
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameService gameService;

	@GetMapping("/findAllOpenGame")
	@ResponseBody
	public Result findAllOpenGame() {
		return Result.success().setData(gameService.findAllOpenGame());
	}

	@GetMapping("/findGamePlayAndNumLocateByGameCode")
	@ResponseBody
	public Result findGamePlayAndNumLocateByGameCode(String gameCode) {
		return Result.success().setData(gameService.findGamePlayDetailsByGameCode(gameCode));
	}

	@GetMapping("/findAllGameCategory")
	@ResponseBody
	public Result findAllGameCategory() {
		return Result.success().setData(gameService.findAllGameCategory());
	}

	@GetMapping("/findAllGameSituation")
	@ResponseBody
	public Result findAllGameSituation() {
		return Result.success().setData(gameService.findAllGameSituation());
	}

	@GetMapping("/findGameSituationByGameCategoryId")
	@ResponseBody
	public Result findGameSituationByGameCategoryId(String gameCategoryId) {
		return Result.success().setData(gameService.findGameSituationByGameCategoryId(gameCategoryId));
	}

}
