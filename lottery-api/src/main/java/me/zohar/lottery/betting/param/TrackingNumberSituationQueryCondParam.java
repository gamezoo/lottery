package me.zohar.lottery.betting.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class TrackingNumberSituationQueryCondParam extends PageParam {

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 开始日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

	/**
	 * 结束日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	
	private String state;
	
	private String userAccountId;

}
