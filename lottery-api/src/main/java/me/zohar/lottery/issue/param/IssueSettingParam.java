package me.zohar.lottery.issue.param;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.issue.domain.IssueSetting;

@Data
public class IssueSettingParam {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 所属游戏id
	 */
	@NotBlank
	private String gameId;

	/**
	 * 日期格式
	 */
	@NotBlank
	private String dateFormat;

	/**
	 * 期号格式
	 */
	@NotBlank
	private String issueFormat;

	/**
	 * 期号生成规则集合
	 */
	@NotEmpty
	private List<IssueGenerateRuleParam> issueGenerateRules;

	public IssueSetting convertToPo() {
		IssueSetting po = new IssueSetting();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
