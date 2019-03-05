package me.zohar.lottery.dictconfig.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.dictconfig.domain.ConfigItem;

@Data
public class ConfigItemVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private String id;

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

	public static List<ConfigItemVO> convertFor(List<ConfigItem> configItems) {
		if (CollectionUtil.isEmpty(configItems)) {
			return new ArrayList<>();
		}
		List<ConfigItemVO> vos = new ArrayList<>();
		for (ConfigItem configItem : configItems) {
			vos.add(convertFor(configItem));
		}
		return vos;
	}

	public static ConfigItemVO convertFor(ConfigItem configItem) {
		if (configItem == null) {
			return null;
		}
		ConfigItemVO vo = new ConfigItemVO();
		BeanUtils.copyProperties(configItem, vo);
		return vo;
	}

}
