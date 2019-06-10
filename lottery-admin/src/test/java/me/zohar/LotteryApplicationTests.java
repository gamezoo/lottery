package me.zohar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import me.zohar.lottery.statisticalanalysis.param.AccountProfitAndLossQueryCondParam;
import me.zohar.lottery.statisticalanalysis.service.StatisticalAnalysisService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryApplicationTests {

	@Autowired
	private StatisticalAnalysisService statisticalAnalysisService;

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void contextLoads() {
		AccountProfitAndLossQueryCondParam param = new AccountProfitAndLossQueryCondParam();
		param.setPageNum(1);
		param.setPageSize(10);
		param.setCurrentAccountId("1100805062633979904");
		// param.setAccountType("member");
		param.setStartTime(DateUtil.parse("2019-06-09"));
		param.setEndTime(DateUtil.parse("2019-06-09"));
		statisticalAnalysisService.findAccountProfitAndLossByPage(param);
	}

}
