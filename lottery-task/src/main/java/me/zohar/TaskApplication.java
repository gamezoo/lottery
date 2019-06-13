package me.zohar;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import me.zohar.lottery.issue.service.IssueService;

@SpringBootApplication
public class TaskApplication implements ApplicationRunner {

	@Autowired
	private IssueService issueService;

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		issueService.generateIssue(DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 0));
	}

}
