package me.zohar.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.game.domain.Game;
import me.zohar.lottery.game.domain.GamePlay;
import me.zohar.lottery.game.repo.GameRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameTest {

	@Autowired
	private GameRepo gameRepo;

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void contextLoads() {
		Game cqssc = new Game();
		cqssc.setId(IdUtils.getId());
		cqssc.setGameCode("CQSSC");
		cqssc.setGameName("重庆时时彩");
		cqssc.setOrderNo(1d);
		cqssc.setState("1");
		gameRepo.save(cqssc);

		Game xjssc = new Game();
		xjssc.setId(IdUtils.getId());
		xjssc.setGameCode("XJSSC");
		xjssc.setGameName("新疆时时彩");
		xjssc.setOrderNo(2d);
		xjssc.setState("1");
		gameRepo.save(xjssc);

	}

}
