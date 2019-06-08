package me.zohar.lottery.useraccount.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

/**
 * 下级账号查询条件入参
 * 
 * @author zohar
 * @date 2019年6月9日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LowerLevelAccountQueryCondParam extends PageParam {

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
	 * 查询范围;10:所有账号,20:指定账号及直接下级
	 */
	@NotBlank
	private String queryScope;

}
