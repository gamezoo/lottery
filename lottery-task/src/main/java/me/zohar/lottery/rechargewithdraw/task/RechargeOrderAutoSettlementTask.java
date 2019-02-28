package me.zohar.lottery.rechargewithdraw.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.rechargewithdraw.service.RechargeService;

/**
 * 充值订单自动结算定时任务
 * 
 * @author zohar
 * @date 2019年1月21日
 *
 */
@Component
@Slf4j
public class RechargeOrderAutoSettlementTask {

	@Autowired
	private RechargeService rechargeService;

	@Scheduled(fixedRate = 60000)
	public void execute() {
		try {
			log.info("充值订单自动结算定时任务start");
			rechargeService.rechargeOrderAutoSettlement();
			log.info("充值订单自动结算定时任务end");
		} catch (Exception e) {
			log.error("充值订单自动结算定时任务", e);
		}
	}

}
