package me.zohar.lottery.dictconfig;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.zohar.lottery.dictconfig.service.ConfigService;
import me.zohar.lottery.dictconfig.vo.ConfigItemVO;

@Component
public class ConfigHolder {

	@Autowired
	private ConfigService configService;

	private static ConfigHolder configHolder;

	@PostConstruct
	public void init() {
		configHolder = this;
		configHolder.configService = this.configService;
	}

	public static String getConfigValue(String configItemCode) {
		ConfigItemVO configItemVO = configHolder.configService.findConfigItemByConfigCode(configItemCode);
		if (configItemVO == null) {
			return null;
		}
		return configItemVO.getConfigValue();
	}

}
