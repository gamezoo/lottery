package me.zohar.lottery.information.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "information_crawler")
@DynamicInsert(true)
@DynamicUpdate(true)
public class InformationCrawler {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 脚本
	 */
	private String script;

	private Date createTime;

}
