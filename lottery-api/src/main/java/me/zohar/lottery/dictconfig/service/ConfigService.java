package me.zohar.lottery.dictconfig.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.dictconfig.ConfigHolder;
import me.zohar.lottery.dictconfig.domain.ConfigItem;
import me.zohar.lottery.dictconfig.repo.ConfigItemRepo;
import me.zohar.lottery.dictconfig.vo.ConfigItemVO;

@Service
@Slf4j
public class ConfigService {

	@Autowired
	private ConfigItemRepo configItemRepo;

	/**
	 * 同步配置到缓存
	 */
	@Transactional(readOnly = true)
	public void syncConfigToCache() {
		log.warn("同步配置");
		ConfigHolder.clearConfig();
		List<ConfigItem> configItems = configItemRepo.findAll();
		for (ConfigItem configItem : configItems) {
			ConfigHolder.putConfig(ConfigItemVO.convertFor(configItem));
		}
	}

}
