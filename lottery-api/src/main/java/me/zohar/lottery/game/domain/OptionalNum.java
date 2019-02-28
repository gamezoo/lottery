package me.zohar.lottery.game.domain;

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
 * 可选号码
 * 
 * @author zohar
 * @date 2019年2月27日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "optional_num", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class OptionalNum {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 号码
	 */
	private String num;

	/**
	 * 赔率
	 */
	private Double odds;

	/**
	 * 排序号
	 */
	private Double orderNo;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	/**
	 * 对应号位id
	 */
	@Column(name = "num_locate_id", length = 32)
	private String numLocateId;

}
