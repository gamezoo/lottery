package me.zohar.lottery.game.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizErrorCode;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.game.domain.Game;
import me.zohar.lottery.game.domain.GamePlay;
import me.zohar.lottery.game.domain.NumLocate;
import me.zohar.lottery.game.param.GamePlayParam;
import me.zohar.lottery.game.param.NumLocateParam;
import me.zohar.lottery.game.repo.GamePlayRepo;
import me.zohar.lottery.game.repo.GameRepo;
import me.zohar.lottery.game.repo.NumLocateRepo;
import me.zohar.lottery.game.vo.GamePlayVO;
import me.zohar.lottery.game.vo.GameVO;
import me.zohar.lottery.game.vo.NumLocateVO;

@Service
public class GameService {

	@Autowired
	private GameRepo gameRepo;

	@Autowired
	private GamePlayRepo gamePlayRepo;

	@Autowired
	private NumLocateRepo numLocateRepo;

	@Transactional(readOnly = true)
	public List<GameVO> findAllGame() {
		List<Game> games = gameRepo.findAll(Sort.by(Sort.Order.asc("orderNo")));
		return GameVO.convertFor(games);
	}

	@Transactional(readOnly = true)
	public List<GamePlayVO> findGamePlayByGameCode(String gameCode) {
		List<GamePlayVO> vos = new ArrayList<>();
		List<GamePlay> gamePlays = gamePlayRepo.findByGameCodeOrderByOrderNo(gameCode);
		for (GamePlay gamePlay : gamePlays) {
			GamePlayVO vo = GamePlayVO.convertFor(gamePlay);
			vos.add(vo);
		}
		return vos;
	}

	@Transactional(readOnly = true)
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

	@Transactional(readOnly = true)
	public GamePlayVO findGamePlayDetailsById(String id) {
		GamePlay gamePlay = gamePlayRepo.getOne(id);
		GamePlayVO vo = GamePlayVO.convertFor(gamePlay);
		vo.setNumLocates(NumLocateVO.convertFor(gamePlay.getNumLocates()));
		return vo;
	}

	@Transactional
	public void updateGamePlayState(String id, String state) {
		GamePlay gamePlay = gamePlayRepo.getOne(id);
		gamePlay.setState(state);
		gamePlayRepo.save(gamePlay);
	}

	@Transactional
	public void delGamePlayById(String id) {
		List<NumLocate> numLocates = numLocateRepo.findByGamePlayId(id);
		numLocateRepo.deleteAll(numLocates);
		GamePlay gamePlay = gamePlayRepo.getOne(id);
		gamePlayRepo.delete(gamePlay);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateGamePlay(GamePlayParam gamePlayParam) {
		// 新增
		if (StrUtil.isBlank(gamePlayParam.getId())) {
			GamePlay existGamePlay = gamePlayRepo.findByGameCodeAndGamePlayCode(gamePlayParam.getGameCode(),
					gamePlayParam.getGamePlayCode());
			if (existGamePlay != null) {
				throw new BizException(BizErrorCode.游戏玩法代码已存在.getCode(), BizErrorCode.游戏玩法代码已存在.getMsg());
			}
			GamePlay gamePlay = gamePlayParam.convertToPo();
			gamePlayRepo.save(gamePlay);
			if (CollectionUtil.isEmpty(gamePlayParam.getNumLocates())) {
				return;
			}
			for (NumLocateParam numLocateParam : gamePlayParam.getNumLocates()) {
				NumLocate numLocate = numLocateParam.convertToPo();
				numLocate.setGamePlayId(gamePlay.getId());
				numLocateRepo.save(numLocate);
			}
		}
		// 修改
		else {
			GamePlay existGamePlay = gamePlayRepo.findByGameCodeAndGamePlayCode(gamePlayParam.getGameCode(),
					gamePlayParam.getGamePlayCode());
			if (existGamePlay != null && !existGamePlay.getId().equals(gamePlayParam.getId())) {
				throw new BizException(BizErrorCode.游戏玩法代码已存在.getCode(), BizErrorCode.游戏玩法代码已存在.getMsg());
			}
			List<NumLocate> numLocates = numLocateRepo.findByGamePlayId(gamePlayParam.getId());
			numLocateRepo.deleteAll(numLocates);

			GamePlay gamePlay = gamePlayRepo.getOne(gamePlayParam.getId());
			BeanUtils.copyProperties(gamePlayParam, gamePlay);
			gamePlayRepo.save(gamePlay);
			if (CollectionUtil.isEmpty(gamePlayParam.getNumLocates())) {
				return;
			}
			for (NumLocateParam numLocateParam : gamePlayParam.getNumLocates()) {
				NumLocate numLocate = numLocateParam.convertToPo();
				numLocate.setGamePlayId(gamePlay.getId());
				numLocateRepo.save(numLocate);
			}
		}
	}

}
