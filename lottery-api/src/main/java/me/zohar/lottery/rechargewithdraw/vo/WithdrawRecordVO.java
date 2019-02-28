package me.zohar.lottery.rechargewithdraw.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.rechargewithdraw.domain.WithdrawRecord;

@Data
public class WithdrawRecordVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 提现金额
	 */
	private Double withdrawAmount;

	/**
	 * 开户银行
	 */
	private String openAccountBank;

	/**
	 * 开户人姓名
	 */
	private String accountHolder;

	/**
	 * 银行卡账号
	 */
	private String bankCardAccount;
	
	/**
	 * 提交时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date submitTime;

	/**
	 * 状态
	 */
	private String state;

	private String stateName;
	
	/**
	 * 备注
	 */
	private String note;
	
	/**
	 * 审核时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approvalTime;
	
	/**
	 * 确认到帐时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date confirmCreditedTime;

	/**
	 * 用户账号id
	 */
	private String userAccountId;

	/**
	 * 用户名
	 */
	private String userName;

	public static List<WithdrawRecordVO> convertFor(List<WithdrawRecord> withdrawRecords) {
		if (CollectionUtil.isEmpty(withdrawRecords)) {
			return new ArrayList<>();
		}
		List<WithdrawRecordVO> vos = new ArrayList<>();
		for (WithdrawRecord withdrawRecord : withdrawRecords) {
			vos.add(convertFor(withdrawRecord));
		}
		return vos;
	}

	public static WithdrawRecordVO convertFor(WithdrawRecord withdrawRecord) {
		if (withdrawRecord == null) {
			return null;
		}
		WithdrawRecordVO vo = new WithdrawRecordVO();
		BeanUtils.copyProperties(withdrawRecord, vo);
		vo.setStateName(DictHolder.getDictItemName("withdrawRecordState", vo.getState()));
		if (withdrawRecord.getUserAccount() != null) {
			vo.setUserName(withdrawRecord.getUserAccount().getUserName());
		}
		return vo;
	}

}
