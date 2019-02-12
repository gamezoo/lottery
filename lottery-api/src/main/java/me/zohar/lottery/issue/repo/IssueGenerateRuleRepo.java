package me.zohar.lottery.issue.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.issue.domain.IssueGenerateRule;

public interface IssueGenerateRuleRepo
		extends JpaRepository<IssueGenerateRule, String>, JpaSpecificationExecutor<IssueGenerateRule> {
	
	List<IssueGenerateRule> findByIssueSettingId(String issueSettingId);

}
