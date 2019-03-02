package me.zohar.dict;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.dictconfig.domain.ConfigItem;
import me.zohar.lottery.dictconfig.repo.ConfigItemRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigTest {

	@Autowired
	private ConfigItemRepo configItemRepo;

	public ConfigItem buildConfigItem(String configTypeCode, String configTypeName, String configCode,
			String configName, String configValue) {
		ConfigItem configItem = new ConfigItem();
		configItem.setId(IdUtils.getId());
		configItem.setConfigTypeCode(configTypeCode);
		configItem.setConfigTypeName(configTypeName);
		configItem.setConfigCode(configCode);
		configItem.setConfigName(configName);
		configItem.setConfigValue(configValue);
		return configItem;
	}

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initMuspayConfig() {
		configItemRepo.save(buildConfigItem("muspay", "muspay聚合支付", "payUrl", "支付接口地址", "https://api.xxv.cn/Pay"));
//		configItemRepo.save(buildConfigItem("common", "通用配置", "site", "地址", "http://5qp7qa.natappfree.cc"));
//		configItemRepo.save(buildConfigItem("muspay", "muspay聚合支付", "fxid", "商户ID", "2019505"));
//		configItemRepo
//				.save(buildConfigItem("muspay", "muspay聚合支付", "secret", "密钥", "jCjWOTvNpXShjyeZEaaSyaNHXKGWeMCu"));
	}

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initRechargeConfig() {
		configItemRepo.save(buildConfigItem("recharge", "充值", "effectiveDuration", "订单有效时长", "300"));
	}
	
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initInviteCodeConfig() {
		configItemRepo.save(buildConfigItem("inviteCode", "邀请码", "effectiveDuration", "邀请码有效时长", "2592000"));
	}

}
