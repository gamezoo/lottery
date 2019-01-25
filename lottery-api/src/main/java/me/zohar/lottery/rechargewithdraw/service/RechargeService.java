package me.zohar.lottery.rechargewithdraw.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.NumberUtil;
import me.zohar.lottery.common.exception.BizErrorCode;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.rechargewithdraw.domain.RechargeOrder;
import me.zohar.lottery.rechargewithdraw.enums.RechargeOrderState;
import me.zohar.lottery.rechargewithdraw.param.MuspayCallbackParam;
import me.zohar.lottery.rechargewithdraw.param.RechargeOrderParam;
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
	private KafkaTemplate<String, String> kafkaTemplate;

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
			throw new BizException(BizErrorCode.签名不正确.getCode(), BizErrorCode.签名不正确.getMsg());
		}
		long payTimestamp = param.getFxtime() * 1000;
		checkOrder(param.getFxddh(), param.getFxfee(), new Date(payTimestamp));
	}

	/**
	 * 核对订单
	 */
	@Transactional
	public void checkOrder(String orderNo, Double rechargeAmount, Date payTime) {
		RechargeOrder order = rechargeOrderRepo.findByOrderNo(orderNo);
		if (order == null) {
			throw new BizException(BizErrorCode.充值订单不存在.getCode(), BizErrorCode.充值订单不存在.getMsg());
		}
		if (RechargeOrderState.充值订单状态_已支付.getCode().equals(order.getOrderState())) {
			return;
		}
		if (order.getRechargeAmount().compareTo(rechargeAmount) != 0) {
			throw new BizException(BizErrorCode.充值金额对不上.getCode(), BizErrorCode.充值金额对不上.getMsg());
		}
		order.setPayTime(payTime);
		order.setDealTime(new Date());
		order.setOrderState(RechargeOrderState.充值订单状态_已支付.getCode());
		rechargeOrderRepo.save(order);
		kafkaTemplate.send(Constant.充值订单_已支付订单单号, order.getOrderNo());
	}

	/**
	 * 充值订单结算
	 */
	@KafkaListener(topics = Constant.充值订单_已支付订单单号)
	@Transactional
	public void rechargeOrderSettlement(String orderNo) {
		System.err.println("rechargeOrderSettlement...");
		RechargeOrder rechargeOrder = rechargeOrderRepo.findByOrderNo(orderNo);
		if (rechargeOrder == null) {
			throw new BizException(BizErrorCode.充值订单不存在.getCode(), BizErrorCode.充值订单不存在.getMsg());
		}
		if (!RechargeOrderState.充值订单状态_已支付.getCode().equals(rechargeOrder.getOrderState())) {
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
			kafkaTemplate.send(Constant.充值订单_已支付订单单号, order.getOrderNo());
		}
	}

	/**
	 * 订单超时处理
	 */
	@Transactional
	public void orderTimeoutDeal() {
		Date now = new Date();
		List<RechargeOrder> orders = rechargeOrderRepo
				.findByOrderStateAndUsefulTimeLessThan(RechargeOrderState.充值订单状态_待支付.getCode(), now);
		for (RechargeOrder order : orders) {
			order.setDealTime(now);
			order.setOrderState(RechargeOrderState.充值订单状态_超时取消.getCode());
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

}
