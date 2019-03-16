package me.zohar.lottery.issue.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zohar.lottery.issue.domain.IssueSetting;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueSettingDetailsVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 所属游戏id
	 */
	private String gameId;

	/**
	 * 日期格式
	 */
	private String dateFormat;

	/**
	 * 期号格式
	 */
	private String issueFormat;

	private List<IssueGenerateRuleVO> issueGenerateRules = new ArrayList<>();

	public static IssueSettingDetailsVO convertFor(IssueSetting issueSetting) {
		if (issueSetting == null) {
			return null;
		}
		IssueSettingDetailsVO vo = new IssueSettingDetailsVO();
		BeanUtils.copyProperties(issueSetting, vo);
		vo.setIssueGenerateRules(IssueGenerateRuleVO.convertFor(issueSetting.getIssueGenerateRules()));
		return vo;
	}

}
