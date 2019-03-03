package me.zohar.lottery.issue.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.issue.service.IssueService;

@Component
@Slf4j
public class GenerateIssueTask {
	
	@Autowired
	private IssueService issueService;

	/**
	 * 每天零点10分执行一次
	 */
//	@Scheduled(cron = "0 10 0 * * ?")
	@Scheduled(fixedRate = 6000)
	public void execute() {
		try {
			log.info("执行生成期号数据定时任务start");
			issueService.generateIssue(new Date());
			log.info("执行生成期号数据定时任务end");
		} catch (Exception e) {
			log.error("执行生成期号数据定时任务发生异常", e);
		}
	}

}
