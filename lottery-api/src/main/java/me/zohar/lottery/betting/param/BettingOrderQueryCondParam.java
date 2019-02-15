package me.zohar.lottery.betting.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

/**
 * 投注订单查询条件入参
 * @author zohar
 * @date 2019年1月19日
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BettingOrderQueryCondParam extends PageParam {
	
	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endTime;
	
	/**
	 * 状态
	 */
	private String state;
	
	/**
	 * 投注人用户账号id
	 */
	private String userAccountId;

}
