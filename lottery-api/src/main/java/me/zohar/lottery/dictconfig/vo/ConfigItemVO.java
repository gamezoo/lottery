package me.zohar.lottery.dictconfig.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.dictconfig.domain.ConfigItem;

@Data
public class ConfigItemVO {
	
	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 配置项类型code
	 */
	private String configTypeCode;

	/**
	 * 配置项类型名称
	 */
	private String configTypeName;
	
	/**
	 * 配置项code
	 */
	private String configCode;

	/**
	 * 配置项名称
	 */
	private String configName;
	
	/**
	 * 配置项值
	 */
	private String configValue;
	
	public static ConfigItemVO convertFor(ConfigItem configItem) {
		if (configItem == null) {
			return null;
		}
		ConfigItemVO vo = new ConfigItemVO();
		BeanUtils.copyProperties(configItem, vo);
		return vo;
	}

}
