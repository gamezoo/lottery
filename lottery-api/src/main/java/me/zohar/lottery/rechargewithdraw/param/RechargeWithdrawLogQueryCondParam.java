package me.zohar.lottery.rechargewithdraw.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

/**
 * 账变日志查询条件
 * 
 * @author zohar
 * @date 2019年1月22日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeWithdrawLogQueryCondParam extends PageParam {

	/**
	 * 订单类型
	 */
	private String orderType;

	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

	/**
	 * 用户账号id
	 */
	private String userAccountId;

}
