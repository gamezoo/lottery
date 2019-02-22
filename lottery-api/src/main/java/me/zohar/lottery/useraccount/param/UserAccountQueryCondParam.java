package me.zohar.lottery.useraccount.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserAccountQueryCondParam extends PageParam {
	
	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 真实姓名
	 */
	private String realName;

}
