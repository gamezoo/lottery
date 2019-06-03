package me.zohar.lottery.betting.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.betting.service.BettingService;

@Component
@Slf4j
public class BettingRebateAutoSettlementTask {

	@Autowired
	private BettingService bettingService;

	@Scheduled(fixedRate = 90000)
	public void execute() {
		try {
			log.info("投注返点自动结算定时任务start");
			bettingService.bettingRebateAutoSettlement();
			log.info("投注返点自动结算定时任务end");
		} catch (Exception e) {
			log.error("投注返点自动结算定时任务", e);
		}
	}

}
