package me.zohar.lottery.issue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.issue.domain.IssueGenerateRule;
import me.zohar.lottery.issue.domain.IssueSetting;
import me.zohar.lottery.issue.param.IssueGenerateRuleParam;
import me.zohar.lottery.issue.param.IssueSettingParam;
import me.zohar.lottery.issue.repo.IssueGenerateRuleRepo;
import me.zohar.lottery.issue.repo.IssueSettingRepo;
import me.zohar.lottery.issue.vo.IssueSettingDetailsVO;

@Service
public class IssueSettingService {

	@Autowired
	private IssueSettingRepo issueSettingRepo;

	@Autowired
	private IssueGenerateRuleRepo issueGenerateRuleRepo;

	public IssueSettingDetailsVO getIssueSettingDetailsByGameId(String gameId) {
		IssueSetting issueSetting = issueSettingRepo.findByGameId(gameId);
		return IssueSettingDetailsVO.convertFor(issueSetting);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateIssueSetting(IssueSettingParam issueSettingParam) {
		// 新增
		if (StrUtil.isBlank(issueSettingParam.getId())) {
			IssueSetting existIssueSetting = issueSettingRepo.findByGameId(issueSettingParam.getGameId());
			if (existIssueSetting != null) {
				throw new BizException(BizError.期号设置已存在);
			}

			IssueSetting issueSetting = issueSettingParam.convertToPo();
			issueSettingRepo.save(issueSetting);
			for (int i = 0; i < issueSettingParam.getIssueGenerateRules().size(); i++) {
				IssueGenerateRuleParam issueGenerateRuleParam = issueSettingParam.getIssueGenerateRules().get(i);
				IssueGenerateRule issueGenerateRule = issueGenerateRuleParam.convertToPo();
				issueGenerateRule.setIssueSettingId(issueSetting.getId());
				issueGenerateRule.setOrderNo((double) (i + 1));
				issueGenerateRuleRepo.save(issueGenerateRule);
			}
		}
		// 修改
		else {
			List<IssueGenerateRule> issueGenerateRules = issueGenerateRuleRepo
					.findByIssueSettingId(issueSettingParam.getId());
			issueGenerateRuleRepo.deleteAll(issueGenerateRules);

			IssueSetting issueSetting = issueSettingRepo.getOne(issueSettingParam.getId());
			issueSetting.updateFormat(issueSettingParam.getDateFormat(), issueSettingParam.getIssueFormat());
			issueSettingRepo.save(issueSetting);
			for (int i = 0; i < issueSettingParam.getIssueGenerateRules().size(); i++) {
				IssueGenerateRuleParam issueGenerateRuleParam = issueSettingParam.getIssueGenerateRules().get(i);
				IssueGenerateRule issueGenerateRule = issueGenerateRuleParam.convertToPo();
				issueGenerateRule.setIssueSettingId(issueSetting.getId());
				issueGenerateRule.setOrderNo((double) (i + 1));
				issueGenerateRuleRepo.save(issueGenerateRule);
			}
		}
	}

}
