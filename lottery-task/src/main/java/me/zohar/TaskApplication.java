package me.zohar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import me.zohar.lottery.dictconfig.service.ConfigService;
import me.zohar.lottery.dictconfig.service.DictService;

@SpringBootApplication
public class TaskApplication implements ApplicationRunner {

	@Autowired
	private DictService dictService;

	@Autowired
	private ConfigService configService;

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		dictService.syncDictToCache();
		configService.syncConfigToCache();
	}

}
