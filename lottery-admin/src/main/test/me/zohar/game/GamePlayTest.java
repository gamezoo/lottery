package me.zohar.game;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.betting.repo.BettingOrderRepo;
import me.zohar.lottery.betting.service.BettingService;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.game.domain.GamePlay;
import me.zohar.lottery.game.domain.NumLocate;
import me.zohar.lottery.game.domain.OptionalNum;
import me.zohar.lottery.game.repo.GamePlayRepo;
import me.zohar.lottery.game.repo.NumLocateRepo;
import me.zohar.lottery.game.repo.OptionalNumRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GamePlayTest {

	@Autowired
	private GamePlayRepo gamePlayRepo;

	@Autowired
	private BettingService bettingRecordService;

	@Autowired
	private BettingOrderRepo bettingOrderRepo;

	@Autowired
	private NumLocateRepo numLocateRepo;
	
	@Autowired
	private OptionalNumRepo optionalNumRepo;

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
		// BettingOrderQueryCondParam param = new BettingOrderQueryCondParam();
		// param.setPageNum(1);
		// param.setPageSize(10);
		// PageResult<BettingRecordVO> pageResult =
		// bettingRecordService.findBettingRecordByPage(param);
		// System.err.println(JSON.toJSONString(pageResult));
	}

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void contextLoads() {
		for (GamePlay gamePlay : gamePlayRepo.findAll()) {
			for (NumLocate numLocate : gamePlay.getNumLocates()) {
				String nums = numLocate.getNums();
				if (StrUtil.isEmpty(nums)) {
					continue;
				}
				double orderNo = 1;
				for (String num : nums.split(",")) {
					OptionalNum optionalNum = new OptionalNum();
					optionalNum.setId(IdUtils.getId());
					optionalNum.setNum(num);
					optionalNum.setOrderNo(orderNo);
					optionalNum.setOdds(gamePlay.getOdds());
					optionalNum.setNumLocateId(numLocate.getId());
					optionalNumRepo.save(optionalNum);
					
					orderNo++;
				}
			}
		}
	}

}
