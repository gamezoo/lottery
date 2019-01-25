package me.zohar.lottery.rechargewithdraw.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.rechargewithdraw.domain.RechargeOrder;

public interface RechargeOrderRepo
		extends JpaRepository<RechargeOrder, String>, JpaSpecificationExecutor<RechargeOrder> {

	List<RechargeOrder> findByOrderStateAndUsefulTimeLessThan(String orderState, Date usefulTime);

	RechargeOrder findByOrderNo(String orderNo);

	/**
	 * 获取所有已支付,未进行结算的充值订单
	 * 
	 * @return
	 */
	List<RechargeOrder> findByPayTimeIsNotNullAndSettlementTimeIsNullOrderBySubmitTime();

}
