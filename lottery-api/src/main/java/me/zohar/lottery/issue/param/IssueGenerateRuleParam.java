package me.zohar.lottery.issue.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.issue.domain.IssueGenerateRule;

@Data
public class IssueGenerateRuleParam {
	
	/**
	 * 开始时间(时分,如10:00)
	 */
	@NotBlank
	private String startTime;

	/**
	 * 时间间隔(分钟)
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false, message = "timeInterval不能少于或等于0")
	private Integer timeInterval;

	/**
	 * 期数
	 */
	@NotNull
	@DecimalMin(value = "1", inclusive = true, message = "issueCount不能小于1")
	private Integer issueCount;
	
	public IssueGenerateRule convertToPo() {
		IssueGenerateRule po = new IssueGenerateRule();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
