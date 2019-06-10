package me.zohar.lottery.statisticalanalysis.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccountProfitAndLossQueryCondParam extends PageParam {

	/**
	 * 当前账号id
	 */
	@NotBlank
	private String currentAccountId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 账号类型
	 */
	private String accountType;

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

}
