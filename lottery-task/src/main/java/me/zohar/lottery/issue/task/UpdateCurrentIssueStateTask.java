package me.zohar.lottery.issue.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.service.IssueService;

@Component
@Slf4j
public class UpdateCurrentIssueStateTask {

	@Autowired
	private IssueService issueService;

//	@Scheduled(fixedRate = 3000)
	public void execute() {
		try {
			log.info("更新游戏状态定时任务start");
			issueService.updateGameCurrentIssueState(Constant.游戏_广东11选5);
			issueService.updateGameCurrentIssueState(Constant.游戏_江西11选5);
			issueService.updateGameCurrentIssueState(Constant.游戏_江苏11选5);
			issueService.updateGameCurrentIssueState(Constant.游戏_上海11选5);
			issueService.updateGameCurrentIssueState(Constant.游戏_浙江11选5);
			
			issueService.updateGameCurrentIssueState(Constant.游戏_重庆时时彩);
			issueService.updateGameCurrentIssueState(Constant.游戏_新疆时时彩);
			issueService.updateGameCurrentIssueState(Constant.游戏_云南时时彩);
			issueService.updateGameCurrentIssueState(Constant.游戏_天津时时彩);
			log.info("更新游戏状态定时任务end");
		} catch (Exception e) {
			log.error("更新游戏状态定时任务发生异常", e);
		}
	}

}
