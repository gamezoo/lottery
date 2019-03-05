package me.zohar.lottery.rechargewithdraw.service;

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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.rechargewithdraw.domain.RechargeOrder;
import me.zohar.lottery.rechargewithdraw.param.MuspayCallbackParam;
import me.zohar.lottery.rechargewithdraw.param.RechargeOrderParam;
import me.zohar.lottery.rechargewithdraw.param.RechargeOrderQueryCondParam;
import me.zohar.lottery.rechargewithdraw.repo.RechargeOrderRepo;
import me.zohar.lottery.rechargewithdraw.utils.Muspay;
import me.zohar.lottery.rechargewithdraw.vo.RechargeOrderVO;
import me.zohar.lottery.useraccount.domain.AccountChangeLog;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.repo.AccountChangeLogRepo;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;

@Service
public class RechargeService {
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RechargeOrderRepo rechargeOrderRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@ParamValid
	public void checkOrderWithMuspay(MuspayCallbackParam param) {
		if (!Muspay.支付成功状态.equals(param.getFxstatus())) {
			return;
		}
		String signature = Muspay.generateCallbackSign(param.getFxstatus(), param.getFxddh(), param.getFxfee());
		if (!signature.equals(param.getFxsign())) {
			throw new BizException(BizError.签名不正确);
		}
		long payTimestamp = param.getFxtime() * 1000;
		checkOrder(param.getFxddh(), Double.parseDouble(param.getFxfee()), new Date(payTimestamp));
	}

	/**
	 * 核对订单
	 */
	@Transactional
	public void checkOrder(String orderNo, Double rechargeAmount, Date payTime) {
		RechargeOrder order = rechargeOrderRepo.findByOrderNo(orderNo);
		if (order == null) {
			throw new BizException(BizError.充值订单不存在);
		}
		if (Constant.充值订单状态_已支付.equals(order.getOrderState())) {
			return;
		}
		if (order.getRechargeAmount().compareTo(rechargeAmount) != 0) {
			throw new BizException(BizError.充值金额对不上);
		}
		order.setPayTime(payTime);
		order.setDealTime(new Date());
		order.setOrderState(Constant.充值订单状态_已支付);
		rechargeOrderRepo.save(order);
		redisTemplate.opsForList().leftPush(Constant.充值订单_已支付订单单号, order.getOrderNo());
	}

	/**
	 * 充值订单结算
	 */
	@Transactional
	public void rechargeOrderSettlement(String orderNo) {
		RechargeOrder rechargeOrder = rechargeOrderRepo.findByOrderNo(orderNo);
		if (rechargeOrder == null) {
			throw new BizException(BizError.充值订单不存在);
		}
		if (!Constant.充值订单状态_已支付.equals(rechargeOrder.getOrderState())) {
			return;
		}
		rechargeOrder.setSettlementTime(new Date());
		rechargeOrderRepo.save(rechargeOrder);
		UserAccount userAccount = rechargeOrder.getUserAccount();
		double balance = userAccount.getBalance() + rechargeOrder.getRechargeAmount();
		userAccount.setBalance(NumberUtil.round(balance, 4).doubleValue());
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithRecharge(userAccount, rechargeOrder));
	}

	@Transactional(readOnly = true)
	public void rechargeOrderAutoSettlement() {
		List<RechargeOrder> orders = rechargeOrderRepo.findByPayTimeIsNotNullAndSettlementTimeIsNullOrderBySubmitTime();
		for (RechargeOrder order : orders) {
			redisTemplate.opsForList().leftPush(Constant.充值订单_已支付订单单号, order.getOrderNo());
		}
	}

	/**
	 * 订单超时处理
	 */
	@Transactional
	public void orderTimeoutDeal() {
		Date now = new Date();
		List<RechargeOrder> orders = rechargeOrderRepo.findByOrderStateAndUsefulTimeLessThan(Constant.充值订单状态_待支付, now);
		for (RechargeOrder order : orders) {
			order.setDealTime(now);
			order.setOrderState(Constant.充值订单状态_超时取消);
			rechargeOrderRepo.save(order);
		}
	}

	@Transactional
	public RechargeOrderVO generateRechargeOrder(RechargeOrderParam param) {
		RechargeOrder rechargeOrder = param.convertToPo();
		String payUrl = Muspay.sendRequest(rechargeOrder.getOrderNo(), rechargeOrder.getRechargeAmount(),
				rechargeOrder.getRechargeWayCode());
		rechargeOrder.setPayUrl(payUrl);
		rechargeOrderRepo.save(rechargeOrder);
		return RechargeOrderVO.convertFor(rechargeOrder);
	}

	@Transactional(readOnly = true)
	public PageResult<RechargeOrderVO> findRechargeOrderByPage(RechargeOrderQueryCondParam param) {
		Specification<RechargeOrder> spec = new Specification<RechargeOrder>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<RechargeOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getOrderNo())) {
					predicates.add(builder.equal(root.get("orderNo"), param.getOrderNo()));
				}
				if (StrUtil.isNotBlank(param.getRechargeWayCode())) {
					predicates.add(builder.equal(root.get("rechargeWayCode"), param.getRechargeWayCode()));
				}
				if (StrUtil.isNotBlank(param.getOrderState())) {
					predicates.add(builder.equal(root.get("orderState"), param.getOrderState()));
				}
				if (param.getSubmitStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.beginOfDay(param.getSubmitStartTime())));
				}
				if (param.getSubmitEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.endOfDay(param.getSubmitEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<RechargeOrder> result = rechargeOrderRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<RechargeOrderVO> pageResult = new PageResult<>(RechargeOrderVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	/**
	 * 取消订单
	 * 
	 * @param id
	 */
	@ParamValid
	@Transactional
	public void cancelOrder(@NotBlank String id) {
		RechargeOrder rechargeOrder = rechargeOrderRepo.getOne(id);
		if (!Constant.充值订单状态_待支付.equals(rechargeOrder.getOrderState())) {
			throw new BizException(BizError.只有待支付状态的充值订单才能取消);
		}
		rechargeOrder.setOrderState(Constant.充值订单状态_人工取消);
		rechargeOrder.setDealTime(new Date());
		rechargeOrderRepo.save(rechargeOrder);
	}

}
