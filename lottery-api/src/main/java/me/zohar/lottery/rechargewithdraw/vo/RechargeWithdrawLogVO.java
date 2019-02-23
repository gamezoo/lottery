package me.zohar.lottery.rechargewithdraw.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.rechargewithdraw.domain.RechargeWithdrawLog;

@Data
public class RechargeWithdrawLogVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 订单类型
	 */
	private String orderType;

	/**
	 * 订单类型名称:充值,提现
	 */
	private String orderTypeName;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 提交时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date submitTime;

	/**
	 * 订单状态
	 */
	private String orderState;

	/**
	 * 订单状态名称
	 */
	private String orderStateName;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 用户账号id
	 */
	private String userAccountId;

	public static List<RechargeWithdrawLogVO> convertFor(List<RechargeWithdrawLog> rechargeWithdrawLogs) {
		if (CollectionUtil.isEmpty(rechargeWithdrawLogs)) {
			return new ArrayList<>();
		}
		List<RechargeWithdrawLogVO> vos = new ArrayList<>();
		for (RechargeWithdrawLog rechargeWithdrawLog : rechargeWithdrawLogs) {
			vos.add(convertFor(rechargeWithdrawLog));
		}
		return vos;
	}

	public static RechargeWithdrawLogVO convertFor(RechargeWithdrawLog rechargeWithdrawLog) {
		if (rechargeWithdrawLog == null) {
			return null;
		}
		RechargeWithdrawLogVO vo = new RechargeWithdrawLogVO();
		BeanUtils.copyProperties(rechargeWithdrawLog, vo);
		vo.setOrderTypeName(DictHolder.getDictItemName("rechargeWithdrawLogOrderType", vo.getOrderType()));
		if (Constant.充提日志订单类型_充值.equals(vo.getOrderType())) {
			vo.setOrderStateName(DictHolder.getDictItemName("rechargeOrderState", vo.getOrderState()));
		}
		return vo;
	}

}
