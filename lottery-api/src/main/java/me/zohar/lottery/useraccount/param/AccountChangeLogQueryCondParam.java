package me.zohar.lottery.useraccount.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccountChangeLogQueryCondParam extends PageParam {

	/**
	 * 游戏代码
	 */
	private String gameCode;

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
	 * 账变类型代码
	 */
	private String accountChangeTypeCode;

	/**
	 * 用户账号id
	 */
	private String userAccountId;

}
