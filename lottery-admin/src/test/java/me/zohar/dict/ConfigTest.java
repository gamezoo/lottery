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

	public ConfigItem buildConfigItem(String configCode, String configName, String configValue) {
		ConfigItem configItem = new ConfigItem();
		configItem.setId(IdUtils.getId());
		configItem.setConfigCode(configCode);
		configItem.setConfigName(configName);
		configItem.setConfigValue(configValue);
		return configItem;
	}

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initMuspayConfig() {
		configItemRepo.save(buildConfigItem("muspay.asynNoticeUrl", "muspay聚合支付-异步通知地址",
				"http://37shwt.natappfree.cc/recharge/muspayCallback"));
		configItemRepo.save(buildConfigItem("muspay.ssynNoticeUrl", "muspay聚合支付-同步通知地址",
				"http://37shwt.natappfree.cc/pay-success"));
	}

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initRechargeConfig() {
		configItemRepo.save(buildConfigItem("recharge.effectiveDuration", "充值-订单有效时长", "300"));
	}

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initInviteCodeConfig() {
		configItemRepo.save(buildConfigItem("inviteCode.effectiveDuration", "邀请码-邀请码有效时长", "2592000"));
	}

}
