package me.zohar.lottery.betting.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.agent.domain.RebateAndOdds;
import me.zohar.lottery.agent.repo.RebateAndOddsRepo;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.betting.domain.BettingRebate;
import me.zohar.lottery.betting.domain.BettingRecord;
import me.zohar.lottery.betting.param.BettingOrderQueryCondParam;
import me.zohar.lottery.betting.param.BettingRecordParam;
import me.zohar.lottery.betting.param.ChangeOrderParam;
import me.zohar.lottery.betting.param.LowerLevelBettingOrderQueryCondParam;
import me.zohar.lottery.betting.param.PlaceOrderParam;
import me.zohar.lottery.betting.repo.BettingOrderRepo;
import me.zohar.lottery.betting.repo.BettingRebateRepo;
import me.zohar.lottery.betting.repo.BettingRecordRepo;
import me.zohar.lottery.betting.vo.BettingOrderDetailsVO;
import me.zohar.lottery.betting.vo.BettingOrderInfoVO;
import me.zohar.lottery.betting.vo.BettingRecordVO;
import me.zohar.lottery.betting.vo.WinningRankVO;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.utils.ThreadPoolUtils;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.game.domain.GamePlay;
import me.zohar.lottery.game.repo.GamePlayRepo;
import me.zohar.lottery.issue.domain.Issue;
import me.zohar.lottery.issue.enums.GamePlayEnum;
import me.zohar.lottery.issue.repo.IssueRepo;
import me.zohar.lottery.useraccount.domain.AccountChangeLog;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.repo.AccountChangeLogRepo;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;

@Validated
@Service
@Slf4j
public class BettingService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private BettingOrderRepo bettingOrderRepo;

	@Autowired
	private BettingRecordRepo bettingRecordRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@Autowired
	private GamePlayRepo gamePlayRepo;

	@Autowired
	private IssueRepo issueRepo;

	@Autowired
	private BettingRebateRepo bettingRebateRepo;

	@Autowired
	private RebateAndOddsRepo rebateAndOddsRepo;

	@Transactional(readOnly = true)
	public List<WinningRankVO> findTop50WinningRank() {
		List<BettingOrder> bettingOrders = bettingOrderRepo
				.findTop50ByBettingTimeGreaterThanAndStateOrderByTotalWinningAmountDesc(DateUtil.beginOfDay(new Date()),
						Constant.投注订单状态_已中奖);
		if (bettingOrders.size() < 50) {
			bettingOrders = bettingOrderRepo.findTop50ByStateOrderByTotalWinningAmountDesc(Constant.投注订单状态_已中奖);
		}
		return WinningRankVO.convertFor(bettingOrders);
	}

	@Transactional(readOnly = true)
	public BettingOrderDetailsVO findMyOrLowerLevelBettingOrderDetails(String id, String userAccountId) {
		BettingOrderDetailsVO vo = findBettingOrderDetails(id);
		UserAccount bettingOrderAccount = userAccountRepo.getOne(vo.getUserAccountId());
		UserAccount currentAccount = userAccountRepo.getOne(userAccountId);
		// 说明该投注账号不是当前账号的下级账号
		if (!bettingOrderAccount.getAccountLevelPath().startsWith(currentAccount.getAccountLevelPath())) {
			throw new BizException(BizError.无权查看投注记录);
		}
		return vo;
	}

	@Transactional(readOnly = true)
	public BettingOrderDetailsVO findBettingOrderDetails(String id) {
		BettingOrder bettingOrder = bettingOrderRepo.getOne(id);
		return BettingOrderDetailsVO.convertFor(bettingOrder);
	}

	/**
	 * 分页获取我的投注订单信息
	 * 
	 * @param param
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageResult<BettingOrderInfoVO> findMyBettingOrderInfoByPage(BettingOrderQueryCondParam param) {
		if (StrUtil.isBlank(param.getUserAccountId())) {
			throw new BizException(BizError.无权查看投注记录);
		}
		return findBettingOrderInfoByPage(param);
	}

	/**
	 * 分页获取投注订单信息
	 * 
	 * @param param
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageResult<BettingOrderInfoVO> findBettingOrderInfoByPage(BettingOrderQueryCondParam param) {
		Specification<BettingOrder> spec = new Specification<BettingOrder>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<BettingOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getOrderNo())) {
					predicates.add(builder.equal(root.get("orderNo"), param.getOrderNo()));
				}
				if (StrUtil.isNotEmpty(param.getGameCode())) {
					predicates.add(builder.equal(root.get("gameCode"), param.getGameCode()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("bettingTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("bettingTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				if (StrUtil.isNotEmpty(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (StrUtil.isNotEmpty(param.getUserAccountId())) {
					predicates.add(builder.equal(root.get("userAccountId"), param.getUserAccountId()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<BettingOrder> result = bettingOrderRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("bettingTime"))));
		PageResult<BettingOrderInfoVO> pageResult = new PageResult<>(BettingOrderInfoVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	/**
	 * 获取当天最新5次投注记录
	 */
	@Transactional(readOnly = false)
	public List<BettingRecordVO> findTodayLatestThe5TimeBettingRecord(String userAccountId, String gameCode) {
		Date bettingTime = DateUtil.beginOfDay(new Date());
		List<BettingRecord> bettingRecords = bettingRecordRepo
				.findTop5ByBettingOrder_UserAccountIdAndBettingOrder_GameCodeAndBettingOrder_BettingTimeGreaterThanEqualOrderByBettingOrder_BettingTimeDesc(
						userAccountId, gameCode, bettingTime);
		return BettingRecordVO.convertFor(bettingRecords);
	}

	@ParamValid
	@Transactional
	public String placeOrder(PlaceOrderParam placeOrderParam, String userAccountId) {
		Date now = new Date();
		Issue currentIssue = issueRepo.findTopByGameCodeAndStartTimeLessThanEqualAndEndTimeGreaterThan(
				placeOrderParam.getGameCode(), now, now);
		Issue bettingIssue = issueRepo.findByGameCodeAndIssueNum(placeOrderParam.getGameCode(),
				placeOrderParam.getIssueNum());
		if (currentIssue == null) {
			throw new BizException(BizError.休市中);
		}
		if (currentIssue.getIssueNum() == placeOrderParam.getIssueNum()) {
			long second = DateUtil.between(currentIssue.getEndTime(), now, DateUnit.SECOND);
			if (second <= 30) {
				throw new BizException(BizError.已截止投注);
			}
		} else {
			if (bettingIssue == null) {
				throw new BizException(BizError.期号非法);
			}
			if (bettingIssue.getLotteryDate().getTime() < currentIssue.getLotteryDate().getTime()) {
				throw new BizException(BizError.期号非法);
			}
			if (bettingIssue.getLotteryDate().getTime() > currentIssue.getLotteryDate().getTime()) {
				throw new BizException(BizError.只能追当天的号);
			}
			if (bettingIssue.getIssueNum() < currentIssue.getIssueNum()) {
				throw new BizException(BizError.该期已封盘无法投注);
			}
		}

		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		long totalBettingCount = 0;
		double totalBettingAmount = 0;
		List<BettingRecord> bettingRecords = new ArrayList<>();
		for (BettingRecordParam bettingRecordParam : placeOrderParam.getBettingRecords()) {
			GamePlay gamePlay = gamePlayRepo.findByGameCodeAndGamePlayCode(placeOrderParam.getGameCode(),
					bettingRecordParam.getGamePlayCode());
			if (gamePlay == null) {
				throw new BizException(BizError.游戏玩法不存在);
			}
			if (Constant.游戏玩法状态_禁用.equals(gamePlay.getState())) {
				throw new BizException(BizError.游戏玩法被禁用);
			}
			Double odds = gamePlay.getOdds();
			Double accountOdds = userAccount.getOdds();
			if (odds == null || odds <= 0) {
				throw new BizException(BizError.玩法赔率异常);
			}
			if (placeOrderParam.getRebate() > userAccount.getRebate()) {
				throw new BizException(BizError.返点不能大于账号设置的返点);
			}
			if (placeOrderParam.getRebate() > 0) {
				RebateAndOdds rebateAndOdds = rebateAndOddsRepo.findTopByRebate(
						NumberUtil.round(userAccount.getRebate() - placeOrderParam.getRebate(), 4).doubleValue());
				if (rebateAndOdds == null) {
					throw new BizException(BizError.该返点赔率未设置);
				}
				accountOdds = rebateAndOdds.getOdds();
			}

			odds = NumberUtil.round(odds * accountOdds, 4).doubleValue();
			double bettingAmount = NumberUtil.round(bettingRecordParam.getBettingCount()
					* placeOrderParam.getBaseAmount() * placeOrderParam.getMultiple(), 4).doubleValue();
			bettingRecords.add(bettingRecordParam.convertToPo(bettingAmount, odds));
			totalBettingCount += bettingRecordParam.getBettingCount();
			totalBettingAmount += bettingAmount;
		}
		double balance = NumberUtil.round(userAccount.getBalance() - totalBettingAmount, 4).doubleValue();
		if (userAccount.getBalance() <= 0 || balance < 0) {
			throw new BizException(BizError.余额不足);
		}

		BettingOrder bettingOrder = placeOrderParam.convertToPo(bettingIssue.getId(), totalBettingCount,
				totalBettingAmount, userAccountId);
		bettingOrderRepo.save(bettingOrder);
		for (BettingRecord bettingRecord : bettingRecords) {
			bettingRecord.setBettingOrderId(bettingOrder.getId());
			bettingRecordRepo.save(bettingRecord);
		}
		userAccount.setBalance(balance);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithPlaceOrder(userAccount, bettingOrder));
		return bettingOrder.getId();
	}

	/**
	 * 改单
	 */
	@ParamValid
	@Transactional
	public void changeOrder(List<ChangeOrderParam> params) {
		for (ChangeOrderParam param : params) {
			BettingOrder bettingOrder = bettingOrderRepo.getOne(param.getBettingOrderId());
			UserAccount userAccount = bettingOrder.getUserAccount();
			GamePlay gamePlay = gamePlayRepo.findByGameCodeAndGamePlayCode(bettingOrder.getGameCode(),
					param.getGamePlayCode());
			if (gamePlay == null) {
				throw new BizException(BizError.游戏玩法不存在);
			}
			Double odds = gamePlay.getOdds();
			if (odds == null || odds <= 0) {
				throw new BizException(BizError.玩法赔率异常);
			}
			odds = NumberUtil.round(odds * userAccount.getOdds(), 4).doubleValue();

			BettingRecord bettingRecord = bettingRecordRepo.getOne(param.getBettingRecordId());
			bettingRecord.setGamePlayCode(gamePlay.getGamePlayCode());
			bettingRecord.setOdds(odds);
			bettingRecord.setSelectedNo(param.getSelectedNo());
			bettingRecordRepo.save(bettingRecord);
		}
	}

	/**
	 * 结算
	 */
	@Transactional
	public void settlement(@NotBlank String issueId) {
		Issue issue = issueRepo.getOne(issueId);
		if (issue == null || StrUtil.isEmpty(issue.getLotteryNum())) {
			log.error("当前期号还没开奖;id:{},issueNum:{}", issue.getId(), issue.getLotteryNum());
			return;
		}

		List<BettingOrder> bettingOrders = bettingOrderRepo.findByGameCodeAndIssueNumAndState(issue.getGameCode(),
				issue.getIssueNum(), Constant.投注订单状态_未开奖);
		for (BettingOrder bettingOrder : bettingOrders) {
			String state = Constant.投注订单状态_未中奖;
			double totalWinningAmount = 0;
			Set<BettingRecord> bettingRecords = bettingOrder.getBettingRecords();
			for (BettingRecord bettingRecord : bettingRecords) {
				GamePlayEnum gamePlay = GamePlayEnum
						.getPlay(bettingOrder.getGameCode() + "_" + bettingRecord.getGamePlayCode());
				int winningCount = gamePlay.calcWinningCount(issue.getLotteryNum(), bettingRecord.getSelectedNo());
				if (winningCount > 0) {
					double winningAmount = (bettingRecord.getBettingAmount() * bettingRecord.getOdds() * winningCount);
					bettingRecord.setWinningAmount(NumberUtil.round(winningAmount, 4).doubleValue());
					bettingRecord.setProfitAndLoss(
							NumberUtil.round(winningAmount - bettingRecord.getBettingAmount(), 4).doubleValue());
					bettingRecordRepo.save(bettingRecord);
					state = Constant.投注订单状态_已中奖;
					totalWinningAmount += winningAmount;
				}
			}
			bettingOrder.setLotteryNum(issue.getLotteryNum());
			bettingOrder.setState(state);

			if (Constant.投注订单状态_未中奖.equals(state)) {
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
			generateBettingRebate(bettingOrder);
		}
		ThreadPoolUtils.getBettingRebateSettlementPool().schedule(() -> {
			redisTemplate.opsForList().leftPush(Constant.返点结算期号ID, issueId);
		}, 1, TimeUnit.SECONDS);
	}

	/**
	 * 生成投注返点
	 * 
	 * @param bettingOrder
	 */
	public void generateBettingRebate(BettingOrder bettingOrder) {
		// 自身投注的返点
		if (bettingOrder.getRebateAmount() > 0) {
			BettingRebate bettingRebate = BettingRebate.build(bettingOrder.getRebate(), false,
					bettingOrder.getRebateAmount(), bettingOrder.getId(), bettingOrder.getUserAccountId());
			bettingRebateRepo.save(bettingRebate);
		}
		UserAccount userAccount = bettingOrder.getUserAccount();
		UserAccount superior = bettingOrder.getUserAccount().getInviter();
		while (superior != null) {
			double rebate = NumberUtil.round(superior.getRebate() - userAccount.getRebate(), 4).doubleValue();
			if (rebate < 0) {
				log.error("投注返点异常,下级账号的返点不能大于上级账号;下级账号id:{},上级账号id:{}", userAccount.getId(), superior.getId());
				break;
			}
			double rebateAmount = NumberUtil.round(rebate * 0.01 * bettingOrder.getTotalBettingAmount(), 4)
					.doubleValue();
			BettingRebate bettingRebate = BettingRebate.build(rebate, false, rebateAmount, bettingOrder.getId(),
					superior.getId());
			bettingRebateRepo.save(bettingRebate);
			if (Constant.投注订单状态_已中奖.equals(bettingOrder.getState())) {
				double winningRebateAmount = NumberUtil.round(rebate * 0.01 * bettingOrder.getTotalWinningAmount(), 4)
						.doubleValue();
				BettingRebate winningRebate = BettingRebate.build(rebate, true, winningRebateAmount,
						bettingOrder.getId(), superior.getId());
				bettingRebateRepo.save(winningRebate);
			}
			userAccount = superior;
			superior = superior.getInviter();
		}
	}

	/**
	 * 投注返点结算
	 */
	@Transactional
	public void bettingRebateSettlement(@NotBlank String bettingRebateId) {
		BettingRebate bettingRebate = bettingRebateRepo.getOne(bettingRebateId);
		if (bettingRebate.getSettlementTime() != null) {
			log.warn("当前的投注返点记录已结算,无法重复结算;id:{}", bettingRebateId);
			return;
		}
		bettingRebate.settlement();
		bettingRebateRepo.save(bettingRebate);
		UserAccount userAccount = bettingRebate.getRebateAccount();
		double balance = userAccount.getBalance() + bettingRebate.getRebateAmount();
		userAccount.setBalance(NumberUtil.round(balance, 4).doubleValue());
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithBettingRebate(userAccount, bettingRebate));
	}

	/**
	 * 通知指定的期号进行返点结算
	 * 
	 * @param issueId
	 */
	@Transactional(readOnly = true)
	public void noticeIssueRebateSettlement(@NotBlank String issueId) {
		Issue issue = issueRepo.getOne(issueId);
		List<BettingOrder> bettingOrders = bettingOrderRepo.findByGameCodeAndIssueNum(issue.getGameCode(),
				issue.getIssueNum());
		for (BettingOrder bettingOrder : bettingOrders) {
			List<BettingRebate> bettingRebates = bettingRebateRepo.findByBettingOrderId(bettingOrder.getId());
			for (BettingRebate bettingRebate : bettingRebates) {
				redisTemplate.opsForList().leftPush(Constant.投注返点ID, bettingRebate.getId());
			}
		}
	}

	@Transactional(readOnly = true)
	public void bettingRebateAutoSettlement() {
		List<BettingRebate> bettingRebates = bettingRebateRepo.findBySettlementTimeIsNull();
		for (BettingRebate bettingRebate : bettingRebates) {
			redisTemplate.opsForList().leftPush(Constant.投注返点ID, bettingRebate.getId());
		}
	}

	/**
	 * 撤单
	 */
	@Transactional
	public void cancelOrder(@NotBlank String orderId, @NotBlank String userAccountId) {
		BettingOrder order = bettingOrderRepo.getOne(orderId);
		if (order == null) {
			throw new BizException(BizError.投注订单不存在);
		}
		UserAccount currentAccount = userAccountRepo.getOne(userAccountId);
		if (!Constant.账号类型_管理员.equals(currentAccount.getAccountType())
				&& !order.getUserAccountId().equals(userAccountId)) {
			throw new BizException(BizError.无权撤销投注订单);
		}
		if (!Constant.投注订单状态_未开奖.equals(order.getState())) {
			throw new BizException(BizError.已开奖或已取消无法撤单);
		}
		Issue issue = issueRepo.findByGameCodeAndIssueNum(order.getGameCode(), order.getIssueNum());
		if (new Date().getTime() > issue.getEndTime().getTime()) {
			throw new BizException(BizError.该期已封盘无法撤单);
		}

		order.cancelOrder();
		UserAccount userAccount = order.getUserAccount();
		double balance = userAccount.getBalance() + order.getTotalBettingAmount();
		userAccount.setBalance(NumberUtil.round(balance, 4).doubleValue());
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithBettingCancelOrder(userAccount, order));
	}

	@Transactional
	public void batchCancelOrder(@NotEmpty List<String> orderIds, @NotBlank String userAccountId) {
		for (String orderId : orderIds) {
			cancelOrder(orderId, userAccountId);
		}
	}

	/**
	 * 分页获取下级账号投注订单信息
	 * 
	 * @param param
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageResult<BettingOrderInfoVO> findLowerLevelBettingOrderInfoByPage(
			LowerLevelBettingOrderQueryCondParam param) {
		UserAccount currentAccount = userAccountRepo.getOne(param.getCurrentAccountId());
		UserAccount lowerLevelAccount = currentAccount;
		if (StrUtil.isNotBlank(param.getUserName())) {
			lowerLevelAccount = userAccountRepo.findByUserName(param.getUserName());
			if (lowerLevelAccount == null) {
				throw new BizException(BizError.用户名不存在);
			}
			// 说明该用户名对应的账号不是当前账号的下级账号
			if (!lowerLevelAccount.getAccountLevelPath().startsWith(currentAccount.getAccountLevelPath())) {
				throw new BizException(BizError.不是上级账号无权查看该账号及下级的投注记录);
			}
		}
		String lowerLevelAccountLevelPath = lowerLevelAccount.getAccountLevelPath();

		Specification<BettingOrder> spec = new Specification<BettingOrder>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<BettingOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.like(root.join("userAccount", JoinType.INNER).get("accountLevelPath"),
						lowerLevelAccountLevelPath + "%"));
				if (StrUtil.isNotEmpty(param.getAccountType())) {
					predicates.add(builder.equal(root.join("userAccount", JoinType.INNER).get("accountType"),
							param.getAccountType()));
				}
				if (StrUtil.isNotEmpty(param.getGameCode())) {
					predicates.add(builder.equal(root.get("gameCode"), param.getGameCode()));
				}
				if (param.getIssueNum() != null) {
					predicates.add(builder.equal(root.get("issueNum"), param.getIssueNum()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("bettingTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("bettingTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				if (StrUtil.isNotEmpty(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<BettingOrder> result = bettingOrderRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("bettingTime"))));
		PageResult<BettingOrderInfoVO> pageResult = new PageResult<>(BettingOrderInfoVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
