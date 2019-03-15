package me.zohar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.IdUtil;
import me.zohar.lottery.game.domain.GamePlay;
import me.zohar.lottery.game.domain.NumLocate;
import me.zohar.lottery.game.repo.GamePlayRepo;
import me.zohar.lottery.game.repo.NumLocateRepo;
import me.zohar.lottery.issue.service.IssueService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryApplicationTests {

	@Autowired
	private IssueService issueService;

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void contextLoads() {
		issueService.syncLotteryNum(null, null, null);
		
	}

}
