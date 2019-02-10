package me.zohar.game;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.util.IdUtil;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.betting.param.BettingOrderQueryCondParam;
import me.zohar.lottery.betting.repo.BettingOrderRepo;
import me.zohar.lottery.betting.service.BettingService;
import me.zohar.lottery.betting.vo.BettingRecordVO;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.game.domain.GamePlay;
import me.zohar.lottery.game.repo.GamePlayRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GamePlayTest {

	@Autowired
	private GamePlayRepo gamePlayRepo;

	@Autowired
	private BettingService bettingRecordService;
	
	@Autowired
	private BettingOrderRepo bettingOrderRepo;
	
	
	@Test
	@Transactional(readOnly = true)
	public void query2() {
		BettingOrder bettingOrder = bettingOrderRepo.getOne("1085957982077845504");
		System.err.println(bettingOrder.getGameCode());
		System.err.println(bettingOrder.getBettingRecords().size());
	}

	public GamePlay buildGamePaly() {
		GamePlay gamePlay = new GamePlay();
		gamePlay.setId(IdUtil.objectId());
		gamePlay.setGameCode("CQSSC");
		return gamePlay;
	}

	@Test
	@Transactional(readOnly = true)
	public void query() {
//		BettingOrderQueryCondParam param = new BettingOrderQueryCondParam();
//		param.setPageNum(1);
//		param.setPageSize(10);
//		PageResult<BettingRecordVO> pageResult = bettingRecordService.findBettingRecordByPage(param);
//		System.err.println(JSON.toJSONString(pageResult));
	}

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void contextLoads() {
		for (int i = 45; i < 55; i++) {
			GamePlay gamePaly = buildGamePaly();
			gamePaly.setOrderNo((double) (i + 1));
			gamePlayRepo.save(gamePaly);
		}

	}

}
