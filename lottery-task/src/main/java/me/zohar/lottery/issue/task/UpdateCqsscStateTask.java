package me.zohar.lottery.issue.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.constants.GameCode;
import me.zohar.lottery.issue.service.IssueService;

@Component
@Slf4j
public class UpdateCqsscStateTask {
	
	@Autowired
	private IssueService issueService;
	
	@Scheduled(fixedRate = 3000)
	public void execute() {
		try {
			log.info("更新重庆时时彩状态定时任务start");
			issueService.updateGameState(GameCode.重庆时时彩);
			log.info("更新重庆时时彩状态定时任务end");
		} catch (Exception e) {
			log.error("更新重庆时时彩状态定时任务发生异常", e);
		}
	}

}
