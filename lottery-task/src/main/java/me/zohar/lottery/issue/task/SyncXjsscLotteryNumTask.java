package me.zohar.lottery.issue.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.issue.service.XjsscService;

@Component
@Slf4j
public class SyncXjsscLotteryNumTask {

	@Autowired
	private XjsscService xjsscService;

//	@Scheduled(fixedRate = 5000)
	public void execute() {
		try {
			log.info("执行同步新疆时时彩开奖号码定时任务start");
			xjsscService.syncLotteryNum();
			log.info("执行同步新疆时时彩开奖号码定时任务end");
		} catch (Exception e) {
			log.error("执行同步新疆时时彩开奖号码定时任务发生异常", e);
		}
	}

}
