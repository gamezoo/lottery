package me.zohar.lottery.systemnotice.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "system_notice")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SystemNotice {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String noticeTitle;

	@Lob
	private String noticeContent;

	private Date createTime;

	/**
	 * 发布日期
	 */
	private Date publishDate;

}
