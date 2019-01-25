package me.zohar.lottery.game.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.zohar.lottery.game.domain.GamePlay;
import me.zohar.lottery.game.repo.GamePlayRepo;
import me.zohar.lottery.game.vo.GamePlayVO;
import me.zohar.lottery.game.vo.NumLocateVO;

@Service
public class GamePlayService {

	@Autowired
	private GamePlayRepo gamePlayRepo;

	public List<GamePlayVO> findGamePlayAndNumLocateByGameCode(String gameCode) {
		List<GamePlayVO> vos = new ArrayList<>();
		List<GamePlay> gamePlays = gamePlayRepo.findByGameCodeOrderByOrderNo(gameCode);
		for (GamePlay gamePlay : gamePlays) {
			GamePlayVO vo = GamePlayVO.convertFor(gamePlay);
			vo.setNumLocates(NumLocateVO.convertFor(gamePlay.getNumLocates()));
			vos.add(vo);
		}
		return vos;
	}

}
