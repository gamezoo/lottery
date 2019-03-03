package me.zohar.lottery.dictconfig.param;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.dictconfig.domain.ConfigItem;

@Data
public class ConfigParam {

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
	
	public ConfigItem convertToPo() {
		ConfigItem po = new ConfigItem();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
