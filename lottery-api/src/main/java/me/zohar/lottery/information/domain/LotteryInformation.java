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
@Table(name = "lottery_information")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LotteryInformation {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 发布时间
	 */
	private Date publishTime;

	public static LotteryInformation build(String title, String content, String source) {
		LotteryInformation lotteryInformation = new LotteryInformation();
		lotteryInformation.setTitle(title);
		lotteryInformation.setContent(content);
		lotteryInformation.setSource(source);
		return lotteryInformation;
	}

}
