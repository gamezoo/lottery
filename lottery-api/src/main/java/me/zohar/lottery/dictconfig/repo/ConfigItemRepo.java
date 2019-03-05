package me.zohar.lottery.dictconfig.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.dictconfig.domain.ConfigItem;

public interface ConfigItemRepo extends JpaRepository<ConfigItem, String>, JpaSpecificationExecutor<ConfigItem> {

	ConfigItem findByConfigCode(String configCode);

}
