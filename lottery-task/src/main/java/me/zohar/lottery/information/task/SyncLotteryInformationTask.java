package me.zohar.lottery.information.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.information.service.LotteryInformationService;

@Component
@Slf4j
public class SyncLotteryInformationTask {

	@Autowired
	private LotteryInformationService lotteryInformationService;

	@Scheduled(cron = "0 0 10,20 * * ?")
	public void execute() {
		try {
			log.info("执行同步彩票资讯任务start");
			lotteryInformationService.autoSyncInformation();
			log.info("执行同步彩票资讯任务end");
		} catch (Exception e) {
			log.error("执行同步彩票资讯任务发生异常", e);
		}
	}

}
