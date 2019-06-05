package me.zohar.lottery.betting.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.betting.domain.BettingOrder;
import me.zohar.lottery.betting.domain.TrackingNumberContent;
import me.zohar.lottery.betting.domain.TrackingNumberOrder;
import me.zohar.lottery.betting.domain.TrackingNumberPlan;
import me.zohar.lottery.betting.domain.TrackingNumberSituation;
import me.zohar.lottery.betting.param.BettingRecordParam;
import me.zohar.lottery.betting.param.PlaceOrderParam;
import me.zohar.lottery.betting.param.StartTrackingNumberParam;
import me.zohar.lottery.betting.param.TrackingNumberSituationQueryCondParam;
import me.zohar.lottery.betting.param.StartTrackingNumberParam.TrackingNumberPlanParam;
import me.zohar.lottery.betting.repo.BettingOrderRepo;
import me.zohar.lottery.betting.repo.TrackingNumberContentRepo;
import me.zohar.lottery.betting.repo.TrackingNumberOrderRepo;
import me.zohar.lottery.betting.repo.TrackingNumberPlanRepo;
import me.zohar.lottery.betting.repo.TrackingNumberSituationRepo;
import me.zohar.lottery.betting.vo.TrackingNumberOrderDetailsVO;
import me.zohar.lottery.betting.vo.TrackingNumberSituationVO;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;

@Service
public class TrackingNumberService {

	@Autowired
	private BettingService bettingService;

	@Autowired
	private TrackingNumberOrderRepo trackingNumberOrderRepo;

	@Autowired
	private TrackingNumberPlanRepo trackingNumberPlanRepo;

	@Autowired
	private TrackingNumberSituationRepo trackingNumberSituationRepo;

	@Autowired
	private TrackingNumberContentRepo trackingNumberContentRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private BettingOrderRepo bettingOrderRepo;

	@ParamValid
	@Transactional
	public void startTrackingNumber(StartTrackingNumberParam startTrackingNumberParam, String userAccountId) {
		long totalBettingCount = 0;
		double totalBettingAmount = 0;
		for (BettingRecordParam bettingRecordParam : startTrackingNumberParam.getBettingRecords()) {
			totalBettingCount += bettingRecordParam.getBettingCount();
		}
		for (TrackingNumberPlanParam planParam : startTrackingNumberParam.getPlans()) {
			totalBettingAmount += startTrackingNumberParam.getBaseAmount() * planParam.getMultiple()
					* totalBettingCount;
		}
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		double balance = NumberUtil.round(userAccount.getBalance() - totalBettingAmount, 4).doubleValue();
		if (userAccount.getBalance() <= 0 || balance < 0) {
			throw new BizException(BizError.余额不足);
		}

		// 下单
		for (TrackingNumberPlanParam planParam : startTrackingNumberParam.getPlans()) {
			PlaceOrderParam placeOrderParam = new PlaceOrderParam();
			placeOrderParam.setGameCode(startTrackingNumberParam.getGameCode());
			placeOrderParam.setIssueNum(planParam.getIssueNum());
			placeOrderParam.setBaseAmount(startTrackingNumberParam.getBaseAmount());
			placeOrderParam.setMultiple(planParam.getMultiple());
			placeOrderParam.setTrackingNumberFlag(true);
			placeOrderParam.setRebate(startTrackingNumberParam.getRebate());
			placeOrderParam.setBettingRecords(startTrackingNumberParam.getBettingRecords());
			String bettingOrderId = bettingService.placeOrder(placeOrderParam, userAccountId);
			planParam.setBettingOrderId(bettingOrderId);
		}
		// 保存追号订单
		TrackingNumberOrder trackingNumberOrder = startTrackingNumberParam.convertToPo(
				startTrackingNumberParam.getPlans().get(0).getIssueNum(), totalBettingAmount, userAccountId);
		trackingNumberOrderRepo.save(trackingNumberOrder);
		// 保存追号计划
		for (TrackingNumberPlanParam planParam : startTrackingNumberParam.getPlans()) {
			TrackingNumberPlan plan = planParam.convertToPo(trackingNumberOrder.getId());
			trackingNumberPlanRepo.save(plan);
		}
		// 保存追号内容
		for (BettingRecordParam bettingRecordParam : startTrackingNumberParam.getBettingRecords()) {
			TrackingNumberContent trackingNumberContent = bettingRecordParam
					.convertToTrackingNumberContentPo(trackingNumberOrder.getId());
			trackingNumberContentRepo.save(trackingNumberContent);
		}
	}

	@Transactional(readOnly = true)
	public PageResult<TrackingNumberSituationVO> findMyTrackingNumberSituationByPage(
			TrackingNumberSituationQueryCondParam param) {
		if (StrUtil.isBlank(param.getUserAccountId())) {
			throw new BizException(BizError.无权查看追号记录);
		}
		return findTrackingNumberSituationByPage(param);
	}

	@Transactional(readOnly = true)
	public PageResult<TrackingNumberSituationVO> findTrackingNumberSituationByPage(
			TrackingNumberSituationQueryCondParam param) {
		Specification<TrackingNumberSituation> spec = new Specification<TrackingNumberSituation>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<TrackingNumberSituation> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getOrderNo())) {
					predicates.add(builder.equal(root.get("orderNo"), param.getOrderNo()));
				}
				if (StrUtil.isNotEmpty(param.getGameCode())) {
					predicates.add(builder.equal(root.get("gameCode"), param.getGameCode()));
				}
				if (StrUtil.isNotEmpty(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("trackingNumberTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("trackingNumberTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				if (StrUtil.isNotEmpty(param.getUserAccountId())) {
					predicates.add(builder.equal(root.get("userAccountId"), param.getUserAccountId()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<TrackingNumberSituation> result = trackingNumberSituationRepo.findAll(spec, PageRequest
				.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.asc("trackingNumberTime"))));
		PageResult<TrackingNumberSituationVO> pageResult = new PageResult<>(
				TrackingNumberSituationVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public TrackingNumberOrderDetailsVO findMyTrackingNumberOrderDetails(String id, String userAccountId) {
		TrackingNumberOrderDetailsVO vo = findTrackingNumberOrderDetails(id);
		if (!userAccountId.equals(vo.getUserAccountId())) {
			throw new BizException(BizError.无权查看追号记录);
		}
		return vo;
	}

	@Transactional(readOnly = true)
	public TrackingNumberOrderDetailsVO findTrackingNumberOrderDetails(String id) {
		TrackingNumberSituation order = trackingNumberSituationRepo.getOne(id);
		return TrackingNumberOrderDetailsVO.convertFor(order);
	}

	/**
	 * 中奖即停
	 */
	public void winToStop(String bettingOrderId) {
		BettingOrder bettingOrder = bettingOrderRepo.getOne(bettingOrderId);
		if (!Constant.投注订单状态_已中奖.equals(bettingOrder.getState())) {
			return;
		}
		TrackingNumberPlan plan = trackingNumberPlanRepo.findByBettingOrderId(bettingOrderId);
		if (plan == null) {
			return;
		}
		TrackingNumberOrder trackingNumberOrder = trackingNumberOrderRepo.getOne(plan.getTrackingNumberOrderId());
		if (!trackingNumberOrder.getWinToStop()) {
			return;
		}
	}

	/**
	 * 撤单
	 * 
	 * @param orderId
	 * @param userAccountId
	 */
	@Transactional
	public void cancelOrder(@NotBlank String orderId, @NotBlank String userAccountId) {
		TrackingNumberOrder trackingNumberOrder = trackingNumberOrderRepo.getOne(orderId);
		if (trackingNumberOrder == null) {
			throw new BizException(BizError.追号订单不存在);
		}
		UserAccount currentAccount = userAccountRepo.getOne(userAccountId);
		if (!Constant.账号类型_管理员.equals(currentAccount.getAccountType())
				&& !userAccountId.equals(trackingNumberOrder.getUserAccountId())) {
			throw new BizException(BizError.无权撤销追号订单);
		}
		Date now = new Date();
		List<TrackingNumberPlan> plans = trackingNumberPlanRepo.findByTrackingNumberOrderId(orderId);
		for (TrackingNumberPlan plan : plans) {
			BettingOrder bettingOrder = plan.getBettingOrder();
			if (Constant.投注订单状态_未开奖.equals(bettingOrder.getState())
					&& now.getTime() < bettingOrder.getIssue().getEndTime().getTime()) {
				bettingService.cancelOrder(plan.getBettingOrderId(), userAccountId);
			}
		}
	}

}
