package me.zohar.lottery.issue.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson.JSON;
import com.xxl.mq.client.message.XxlMqMessage;
import com.xxl.mq.client.producer.XxlMqProducer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.betting.service.BettingService;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.common.utils.ThreadPoolUtils;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.domain.Issue;
import me.zohar.lottery.issue.domain.IssueGenerateRule;
import me.zohar.lottery.issue.domain.IssueSetting;
import me.zohar.lottery.issue.param.IssueEditParam;
import me.zohar.lottery.issue.param.ManualLotteryParam;
import me.zohar.lottery.issue.param.SyncLotteryNumMsg;
import me.zohar.lottery.issue.repo.IssueRepo;
import me.zohar.lottery.issue.repo.IssueSettingRepo;
import me.zohar.lottery.issue.vo.IssueVO;

@Validated
@Service
@Slf4j
public class IssueService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private BettingService bettingService;

	@Autowired
	private IssueRepo issueRepo;

	@Autowired
	private IssueSettingRepo issueSettingRepo;

	/**
	 * 同步开奖号码
	 * 
	 * @param gameCode
	 * @param issueNum
	 * @param lotteryNum
	 */
	@Transactional
	public Boolean syncLotteryNum(@NotBlank String gameCode, @NotNull Long issueNum, @NotBlank String lotteryNum) {
		Issue issue = issueRepo.findByGameCodeAndIssueNum(gameCode, issueNum);
		if (issue == null) {
			log.error("当前期号没有生成,请检查定时任务是否发生了异常.gameCode:{},issueNum:{}", gameCode, issueNum);
			return false;
		}
		if (!Constant.期号状态_未开奖.equals(issue.getState())) {
			return false;
		}
		if (!issue.getAutomaticLottery()) {
			log.warn("当前期号没有没有设置自动开奖,同步开奖结果失败.gameCode:{},issueNum:{}", gameCode, issueNum);
			return false;
		}

		issue.syncLotteryNum(lotteryNum);
		issueRepo.save(issue);
		if (issue.getAutomaticSettlement()) {
			ThreadPoolUtils.getLotterySettlementPool().schedule(() -> {
				redisTemplate.opsForList().leftPush(Constant.当前开奖期号ID, issue.getId());
			}, 1, TimeUnit.SECONDS);
		}
		return true;
	}

	/**
	 * 更新游戏当期状态
	 * 
	 * @param gameCode
	 */
	@Transactional
	public void updateGameCurrentIssueState(String gameCode) {
		Date now = new Date();
		Issue issue = issueRepo.findTopByGameCodeAndStartTimeLessThanEqualAndEndTimeGreaterThan(gameCode, now, now);
		if (issue == null) {
			String stateWithRedis = redisTemplate.opsForValue().get(gameCode + Constant.游戏当期状态);
			if (!Constant.游戏当期状态_休市中.equals(stateWithRedis)) {
				redisTemplate.opsForValue().set(gameCode + Constant.游戏当期状态, Constant.游戏当期状态_休市中);
			}
			String currentIssueWithRedis = redisTemplate.opsForValue().get(gameCode + Constant.游戏当前期号);
			if (StrUtil.isNotEmpty(currentIssueWithRedis)) {
				redisTemplate.opsForValue().set(gameCode + Constant.游戏当前期号, "");
			}
			return;
		}
		long second = DateUtil.between(issue.getEndTime(), now, DateUnit.SECOND);
		String state = Constant.游戏当期状态_已截止投注;
		if (second > 30) {
			state = Constant.游戏当期状态_可以投注;
		}
		String stateWithRedis = redisTemplate.opsForValue().get(gameCode + Constant.游戏当期状态);
		if (!state.equals(stateWithRedis)) {
			redisTemplate.opsForValue().set(gameCode + Constant.游戏当期状态, state);
		}
		String currentIssueWithRedis = redisTemplate.opsForValue().get(gameCode + Constant.游戏当前期号);
		if (!("" + issue.getIssueNum()).equals(currentIssueWithRedis)) {
			redisTemplate.opsForValue().set(gameCode + Constant.游戏当前期号, "" + issue.getIssueNum());
		}
	}

	/**
	 * 结算
	 */
	@Transactional
	public void settlement(String issueId) {
		Issue issue = issueRepo.getOne(issueId);
		if (issue == null || StrUtil.isEmpty(issue.getLotteryNum())) {
			log.error("当前期号还没开奖;id:{},issueNum:{}", issue.getId(), issue.getLotteryNum());
			return;
		}
		issue.settlement();
		issueRepo.save(issue);
		bettingService.settlement(issueId);
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
	 * 获取最近50次的开奖期号数据
	 * 
	 * @return
	 */
	public List<IssueVO> findLatelyThe50TimeIssue(String gameCode) {
		List<Issue> issues = issueRepo.findTop50ByGameCodeAndEndTimeLessThanOrderByIssueNumDesc(gameCode, new Date());
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
	 * 获取最近的期号,即当前时间的上一期或少于当前时间的最近一期
	 * 
	 * @return
	 */
	public IssueVO getLatelyIssue(String gameCode) {
		IssueVO currentIssue = getCurrentIssue(gameCode);
		if (currentIssue == null) {
			Issue latelyIssue = issueRepo.findTopByGameCodeAndEndTimeLessThanEqualOrderByEndTimeDesc(gameCode,
					new Date());
			return IssueVO.convertFor(latelyIssue);
		}
		Issue latelyIssue = issueRepo.findTopByGameCodeAndIssueNumLessThanOrderByIssueNumDesc(gameCode,
				currentIssue.getIssueNum());
		return IssueVO.convertFor(latelyIssue);
	}

	@Transactional
	public void generateIssue(Date currentDate) {
		List<IssueSetting> issueSettings = issueSettingRepo.findAll();
		for (IssueSetting issueSetting : issueSettings) {
			for (int i = 0; i < 5; i++) {
				Date lotteryDate = DateUtil.offset(DateUtil.beginOfDay(currentDate), DateField.DAY_OF_MONTH, i);
				List<Issue> issues = issueRepo.findByGameCodeAndLotteryDateOrderByLotteryTimeDesc(
						issueSetting.getGame().getGameCode(), lotteryDate);
				if (CollectionUtil.isNotEmpty(issues)) {
					continue;
				}

				String lotteryDateFormat = DateUtil.format(lotteryDate, issueSetting.getDateFormat());
				Set<IssueGenerateRule> issueGenerateRules = issueSetting.getIssueGenerateRules();
				int count = 0;
				for (IssueGenerateRule issueGenerateRule : issueGenerateRules) {
					Integer issueCount = issueGenerateRule.getIssueCount();
					for (int j = 0; j < issueCount; j++) {
						long issueNum = Long.parseLong(
								lotteryDateFormat + String.format(issueSetting.getIssueFormat(), count + j + 1));
						long issueNumInner = count + j + 1;
						DateTime dateTime = DateUtil.parse(issueGenerateRule.getStartTime(), "hh:mm");
						Date startTime = DateUtil.offset(lotteryDate, DateField.MINUTE,
								dateTime.hour(true) * 60 + dateTime.minute() + j * issueGenerateRule.getTimeInterval());
						Date endTime = DateUtil.offset(startTime, DateField.MINUTE,
								issueGenerateRule.getTimeInterval());

						Issue issue = Issue.builder().id(IdUtils.getId()).gameCode(issueSetting.getGame().getGameCode())
								.lotteryDate(lotteryDate).lotteryTime(endTime).issueNum(issueNum)
								.issueNumInner(issueNumInner).startTime(startTime).endTime(endTime)
								.state(Constant.期号状态_未开奖).automaticLottery(true).automaticSettlement(true).build();
						issueRepo.save(issue);

						Date effectTime = DateUtil.offset(issue.getEndTime(), DateField.SECOND, 2);
						XxlMqProducer.produce(new XxlMqMessage("SYNC_LOTTERY_NUM_" + issue.getGameCode(),
								JSON.toJSONString(new SyncLotteryNumMsg(issue.getGameCode(), issue.getIssueNum(), 0)),
								effectTime));
					}
					count += issueCount;
				}
			}
		}
	}

	/**
	 * 手动开奖
	 * 
	 * @param id
	 * @param lotteryNum
	 */
	@ParamValid
	@Transactional
	public void manualLottery(ManualLotteryParam param) {
		Issue issue = issueRepo.getOne(param.getId());
		if (!Constant.期号状态_未开奖.equals(issue.getState())) {
			throw new BizException(BizError.该期已开奖);
		}
		issue.syncLotteryNum(param.getLotteryNum());
		issueRepo.save(issue);

		if (param.getAutoSettlementFlag()) {
			redisTemplate.opsForList().leftPush(Constant.当前开奖期号ID, param.getId());
		}
	}

	/**
	 * 手动结算
	 * 
	 * @param id
	 */
	@Transactional(readOnly = true)
	public void manualSettlement(String id) {
		Issue issue = issueRepo.getOne(id);
		if (!Constant.期号状态_已开奖.equals(issue.getState())) {
			throw new BizException(BizError.开奖后才能结算);
		}
		redisTemplate.opsForList().leftPush(Constant.当前开奖期号ID, id);
	}

	@ParamValid
	@Transactional
	public void updateIssue(IssueEditParam param) {
		Issue issue = issueRepo.getOne(param.getId());
		if (param.getIssueInvalid()
				&& (Constant.期号状态_已开奖.equals(issue.getState()) || Constant.期号状态_已结算.equals(issue.getState()))) {
			throw new BizException(BizError.期号作废失败);
		}
		if ((Constant.期号状态_未开奖.equals(issue.getState()) || Constant.期号状态_已作废.equals(issue.getState()))) {
			issue.setState(param.getIssueInvalid() ? Constant.期号状态_已作废 : Constant.期号状态_未开奖);
		}
		issue.setAutomaticLottery(param.getAutomaticLottery());
		issue.setAutomaticSettlement(param.getAutomaticSettlement());
		issueRepo.save(issue);
	}

	/**
	 * 获取今日可追号的期号数据
	 * 
	 * @return
	 */
	public List<IssueVO> findTodayTrackingNumberIssue(String gameCode) {
		IssueVO currentIssue = getCurrentIssue(gameCode);
		if (currentIssue == null) {
			return null;
		}
		List<Issue> issues = issueRepo.findByGameCodeAndLotteryDateAndLotteryTimeGreaterThanEqualOrderByLotteryTimeAsc(
				gameCode, currentIssue.getLotteryDate(), currentIssue.getLotteryTime());
		return IssueVO.convertFor(issues);
	}

}
