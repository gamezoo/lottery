package me.zohar.lottery.issue.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.issue.domain.IssueSetting;

public interface IssueSettingRepo extends JpaRepository<IssueSetting, String>, JpaSpecificationExecutor<IssueSetting> {

	IssueSetting findByGameId(String gameId);

}
