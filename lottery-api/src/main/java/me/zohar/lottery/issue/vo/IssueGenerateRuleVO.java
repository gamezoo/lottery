package me.zohar.lottery.issue.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zohar.lottery.issue.domain.IssueGenerateRule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueGenerateRuleVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 开始时间(时分,如10:00)
	 */
	private String startTime;

	/**
	 * 时间间隔(分钟)
	 */
	private Integer timeInterval;

	/**
	 * 期数
	 */
	private Integer issueCount;

	/**
	 * 排序号
	 */
	private Double orderNo;

	/**
	 * 对应期号设置id
	 */
	private String issueSettingId;

	public static List<IssueGenerateRuleVO> convertFor(Collection<IssueGenerateRule> issueGenerateRules) {
		if (CollectionUtil.isEmpty(issueGenerateRules)) {
			return new ArrayList<>();
		}
		List<IssueGenerateRuleVO> vos = new ArrayList<>();
		for (IssueGenerateRule issueGenerateRule : issueGenerateRules) {
			vos.add(convertFor(issueGenerateRule));
		}
		return vos;
	}

	public static IssueGenerateRuleVO convertFor(IssueGenerateRule issueGenerateRule) {
		if (issueGenerateRule == null) {
			return null;
		}
		IssueGenerateRuleVO vo = new IssueGenerateRuleVO();
		BeanUtils.copyProperties(issueGenerateRule, vo);
		return vo;
	}

}
