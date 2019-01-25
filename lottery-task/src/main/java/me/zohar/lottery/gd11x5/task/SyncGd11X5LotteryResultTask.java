package me.zohar.lottery.gd11x5.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.gd11x5.service.Gd11X5LotteryResultService;

/**
 * 同步广东11选5开奖结果task
 * 
 * @author zohar
 * @date 2018年12月23日
 *
 */
@Component
@Slf4j
public class SyncGd11X5LotteryResultTask {

	@Autowired
	private Gd11X5LotteryResultService service;

//	@Scheduled(fixedRate = 5000)
	public void execute() {
		try {
			log.info("执行同步广东11选5开奖结果定时任务start");
			service.syncLotteryResult(new Date());
			log.info("执行同步广东11选5开奖结果定时任务end");
		} catch (Exception e) {
			log.error("执行同步广东11选5开奖结果定时任务发生异常", e);
		}
	}

}
