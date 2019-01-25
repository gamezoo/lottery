package me.zohar.lottery.issue.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.betting.domain.BettingRecord;
import me.zohar.lottery.betting.enums.BettingOrderState;
import me.zohar.lottery.betting.repo.BettingOrderRepo;
import me.zohar.lottery.betting.repo.BettingRecordRepo;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.domain.Issue;
import me.zohar.lottery.issue.enums.GamePlay;
import me.zohar.lottery.issue.repo.IssueRepo;
import me.zohar.lottery.issue.vo.IssueVO;
import me.zohar.lottery.useraccount.domain.AccountChangeLog;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.repo.AccountChangeLogRepo;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;

@Service
@Slf4j
public class IssueService {

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private IssueRepo issueRepo;

	@Autowired
	private BettingOrderRepo bettingOrderRepo;

	@Autowired
	private BettingRecordRepo bettingRecordRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	/**
	 * 同步开奖号码
	 * @param gameCode
	 * @param issueNum
	 * @param lotteryNum
	 */
	@Transactional
	public void syncLotteryNum(String gameCode, Long issueNum, String lotteryNum) {
		Issue latestIssue = issueRepo.findByGameCodeAndIssueNum(gameCode, issueNum);
		if (latestIssue == null) {
			log.error("当前期号没有生成,请检查定时任务是否发生了异常.gameCode:{},issueNum:{}", gameCode, issueNum);
			return;
		}
		if (latestIssue.getSyncTime() != null) {
			return;
		}
		latestIssue.syncLotteryNum(lotteryNum);
		issueRepo.save(latestIssue);
		kafkaTemplate.send(Constant.当前开奖期号ID, latestIssue.getId());
	}

	/**
	 * 更新游戏状态
	 * 
	 * @param gameCode
	 */
	@Transactional
	public void updateGameState(String gameCode) {
		Date now = new Date();
		Issue issue = issueRepo.findTopByGameCodeAndStartTimeLessThanEqualAndEndTimeGreaterThan(gameCode, now, now);
		if (issue == null) {
			String stateWithRedis = redisTemplate.opsForValue().get(gameCode + Constant.游戏状态);
			if (!Constant.游戏状态_休市中.equals(stateWithRedis)) {
				redisTemplate.opsForValue().set(gameCode + Constant.游戏状态, Constant.游戏状态_休市中);
			}
			String currentIssueWithRedis = redisTemplate.opsForValue().get(gameCode + Constant.游戏当前期号);
			if (StrUtil.isNotEmpty(currentIssueWithRedis)) {
				redisTemplate.opsForValue().set(gameCode + Constant.游戏当前期号, "");
			}
			return;
		}
		long second = DateUtil.between(issue.getEndTime(), now, DateUnit.SECOND);
		String state = Constant.游戏状态_已截止投注;
		if (second > 30) {
			state = Constant.游戏状态_可以投注;
		}
		String stateWithRedis = redisTemplate.opsForValue().get(gameCode + Constant.游戏状态);
		if (!state.equals(stateWithRedis)) {
			redisTemplate.opsForValue().set(gameCode + Constant.游戏状态, state);
		}
		String currentIssueWithRedis = redisTemplate.opsForValue().get(gameCode + Constant.游戏当前期号);
		if (!("" + issue.getIssueNum()).equals(currentIssueWithRedis)) {
			redisTemplate.opsForValue().set(gameCode + Constant.游戏当前期号, "" + issue.getIssueNum());
		}
	}

	/**
	 * 结算
	 */
	@KafkaListener(topics = Constant.当前开奖期号ID)
	@Transactional
	public void settlement(String issueId) {
		Issue issue = issueRepo.getOne(issueId);
		if (issue == null || StrUtil.isEmpty(issue.getLotteryNum())) {
			return;
		}
		List<BettingOrder> bettingOrders = bettingOrderRepo.findByGameCodeAndIssueNumAndState(issue.getGameCode(),
				issue.getIssueNum(), BettingOrderState.投注订单状态_未开奖.getCode());
		for (BettingOrder bettingOrder : bettingOrders) {
			String state = BettingOrderState.投注订单状态_未中奖.getCode();
			double totalWinningAmount = 0;
			Set<BettingRecord> bettingRecords = bettingOrder.getBettingRecords();
			for (BettingRecord bettingRecord : bettingRecords) {
				GamePlay gamePlay = GamePlay
						.getPlay(bettingOrder.getGameCode() + "_" + bettingRecord.getGamePlayCode());
				int winningCount = gamePlay.calcWinningCount(issue.getLotteryNum(), bettingRecord.getSelectedNo());
				if (winningCount > 0) {
					double winningAmount = (bettingRecord.getBettingAmount() * bettingRecord.getOdds() * winningCount);
					bettingRecord.setWinningAmount(NumberUtil.round(winningAmount, 4).doubleValue());
					bettingRecord.setProfitAndLoss(
							NumberUtil.round(winningAmount - bettingRecord.getBettingAmount(), 4).doubleValue());
					bettingRecordRepo.save(bettingRecord);
					state = BettingOrderState.投注订单状态_已中奖.getCode();
					totalWinningAmount += winningAmount;
				}
			}
			bettingOrder.setLotteryNum(issue.getLotteryNum());
			bettingOrder.setState(state);
			if (BettingOrderState.投注订单状态_未中奖.getCode().equals(state)) {
				bettingOrderRepo.save(bettingOrder);
			} else {
				bettingOrder.setTotalWinningAmount(NumberUtil.round(totalWinningAmount, 4).doubleValue());
				bettingOrder.setTotalProfitAndLoss(
						NumberUtil.round(totalWinningAmount - bettingOrder.getTotalBettingAmount(), 4).doubleValue());
				bettingOrderRepo.save(bettingOrder);
				UserAccount userAccount = bettingOrder.getUserAccount();
				double balance = userAccount.getBalance() + totalWinningAmount;
				userAccount.setBalance(NumberUtil.round(balance, 4).doubleValue());
				userAccountRepo.save(userAccount);
				accountChangeLogRepo.save(AccountChangeLog.buildWithWinning(userAccount, bettingOrder));
			}

		}
	}

	/**
	 * 获取最近5次的开奖期号数据
	 * 
	 * @return
	 */
	public List<IssueVO> findLatelyThe5TimeIssue(String gameCode) {
		List<Issue> issues = issueRepo.findTop5ByGameCodeAndEndTimeLessThanOrderByIssueNumDesc(gameCode, new Date());
		return IssueVO.convertFor(issues);
	}

	/**
	 * 获取下一期
	 * 
	 * @return
	 */
	public IssueVO getNextIssue(String gameCode) {
		Date now = new Date();
		Issue nextIssue = issueRepo.findTopByGameCodeAndStartTimeGreaterThanOrderByLotteryTimeAsc(gameCode, now);
		return IssueVO.convertFor(nextIssue);
	}

	/**
	 * 获取当前的期号,即当前时间对应的期号
	 * 
	 * @return
	 */
	public IssueVO getCurrentIssue(String gameCode) {
		Date now = new Date();
		Issue currentIssue = issueRepo.findTopByGameCodeAndStartTimeLessThanEqualAndEndTimeGreaterThan(gameCode, now,
				now);
		return IssueVO.convertFor(currentIssue);
	}

	/**
	 * 获取最近的期号,即当前时间的上一期
	 * 
	 * @return
	 */
	public IssueVO getLatelyIssue(String gameCode) {
		IssueVO currentIssue = getCurrentIssue(gameCode);
		if (currentIssue == null) {
			return null;
		}
		Issue latelyIssue = issueRepo.findTopByGameCodeAndIssueNumLessThanOrderByIssueNumDesc(gameCode,
				currentIssue.getIssueNum());
		return IssueVO.convertFor(latelyIssue);
	}

}
