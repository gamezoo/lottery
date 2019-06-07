package me.zohar.lottery;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.betting.service.BettingService;
import me.zohar.lottery.common.utils.ThreadPoolUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.service.IssueService;
import me.zohar.lottery.rechargewithdraw.service.RechargeService;

@Slf4j
@Component
public class RedisMessageListener {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private IssueService issueService;

	@Autowired
	private RechargeService rechargeService;

	@Autowired
	private BettingService bettingService;

	@PostConstruct
	public void init() {
		listenLotterySettlement();
		listenBettingRebateSettlement();
		listenRechargeSettlement();
	}

	public void listenBettingRebateSettlement() {
		new Thread(() -> {
			while (true) {
				try {
					String issueId = redisTemplate.opsForList().rightPop(Constant.返点结算期号ID, 2L, TimeUnit.SECONDS);
					if (StrUtil.isBlank(issueId)) {
						continue;
					}

					ThreadPoolUtils.getLotterySettlementPool().execute(() -> {
						try {
							log.info("系统通知该期进行返点结算,id为{}", issueId);
							bettingService.noticeIssueRebateSettlement(issueId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统通知该期进行返点结算出现异常,id为{0}", issueId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("系统通知该期进行返点结算消息队列异常", e);
				}
			}
		}).start();

		new Thread(() -> {
			while (true) {
				try {
					String bettingRebateId = redisTemplate.opsForList().rightPop(Constant.投注返点ID, 2L, TimeUnit.SECONDS);
					if (StrUtil.isBlank(bettingRebateId)) {
						continue;
					}

					ThreadPoolUtils.getLotterySettlementPool().execute(() -> {
						try {
							log.info("系统进行投注返点结算操作,id为{}", bettingRebateId);
							bettingService.bettingRebateSettlement(bettingRebateId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行投注返点结算操作出现异常,id为{0}", bettingRebateId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("投注返点结算消息队列异常", e);
				}
			}
		}).start();
	}

	public void listenLotterySettlement() {
		new Thread(() -> {
			while (true) {
				try {
					String issueId = redisTemplate.opsForList().rightPop(Constant.当前开奖期号ID, 2L, TimeUnit.SECONDS);
					if (StrUtil.isBlank(issueId)) {
						continue;
					}

					ThreadPoolUtils.getLotterySettlementPool().execute(() -> {
						try {
							log.info("系统进行开奖结算操作,期号id为{}", issueId);
							issueService.settlement(issueId);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行开奖结算操作出现异常,期号id为{0}", issueId), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("开奖结算消息队列异常", e);
				}
			}
		}).start();
	}

	public void listenRechargeSettlement() {
		new Thread(() -> {
			while (true) {
				try {
					String orderNo = redisTemplate.opsForList().rightPop(Constant.充值订单_已支付订单单号, 2L, TimeUnit.SECONDS);
					if (StrUtil.isBlank(orderNo)) {
						continue;
					}

					ThreadPoolUtils.getRechargeSettlementPool().execute(() -> {
						try {
							log.info("系统进行充值结算操作,订单单号为{}", orderNo);
							rechargeService.rechargeOrderSettlement(orderNo);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行充值结算操作出现异常,订单单号为{0}", orderNo), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("充值结算消息队列异常", e);
				}
			}
		}).start();
	}

}
