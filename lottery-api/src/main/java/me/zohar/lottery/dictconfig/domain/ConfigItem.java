package me.zohar.lottery.dictconfig.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置项
 * 
 * @author zohar
 * @date 2019年1月21日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "config_item", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ConfigItem {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
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
	
	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

}
