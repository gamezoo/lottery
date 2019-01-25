package me.zohar.lottery.dictconfig;

import java.util.HashMap;
import java.util.Map;

import me.zohar.lottery.dictconfig.vo.ConfigItemVO;

public class ConfigHolder {

	public static Map<String, ConfigItemVO> configMap = new HashMap<>();

	public static void clearConfig() {
		configMap.clear();
	}

	public static void putConfig(ConfigItemVO configItem) {
		configMap.put(configItem.getConfigTypeCode() + "_" + configItem.getConfigCode(), configItem);
	}

	public static String getConfigValue(String configTypeCode, String configItemCode) {
		ConfigItemVO configItemVO = configMap.get(configTypeCode + "_" + configItemCode);
		if (configItemVO == null) {
			return null;
		}
		return configItemVO.getConfigValue();
	}

}
