package me.zohar.lottery.issue.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 期号生成规则
 * 
 * @author zohar
 * @date 2019年2月13日
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "issue_generate_rule", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class IssueGenerateRule {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 开始时间(时分,如10:00)
	 */
	private String startTime;

	/**
	 * 时间间隔(分钟)
	 */
	private Integer timeInterval;

	/**
	 * 期数
	 */
	private Integer issueCount;

	/**
	 * 排序号
	 */
	private Double orderNo;

	/**
	 * 对应期号设置id
	 */
	@Column(name = "issue_setting_id", length = 32)
	private String issueSettingId;

}
