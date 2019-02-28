package me.zohar.lottery.rechargewithdraw.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class WithdrawRecordQueryCondParam extends PageParam {
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 状态
	 */
	private String state;
	
	/**
	 * 提交开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date submitStartTime;
	
	/**
	 * 提交结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date submitEndTime;

}
