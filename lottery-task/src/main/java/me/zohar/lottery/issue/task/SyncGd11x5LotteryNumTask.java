package me.zohar.lottery.issue.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.issue.service.Gd11x5Service;

@Component
@Slf4j
public class SyncGd11x5LotteryNumTask {

	@Autowired
	private Gd11x5Service gd11x5Service;

	@Scheduled(fixedRate = 6000)
	public void execute() {
		try {
			log.info("执行同步广东11选5开奖号码定时任务start");
			gd11x5Service.syncLotteryNum();
			log.info("执行同步广东11选5开奖号码定时任务end");
		} catch (Exception e) {
			log.error("执行同步广东11选5开奖号码定时任务发生异常", e);
		}
	}

}
