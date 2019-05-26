package me.zohar.lottery.dictconfig.domain;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典项
 * 
 * @author zohar
 * @date 2019年1月19日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "dict_item")
@DynamicInsert(true)
@DynamicUpdate(true)
public class DictItem {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 字典项code
	 */
	private String dictItemCode;

	/**
	 * 字典项名称
	 */
	private String dictItemName;
	
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
	 * 所属字典类型id
	 */
	@Column(name = "dict_type_id", length = 32)
	private String dictTypeId;

	/**
	 * 所属字典类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dict_type_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private DictType dictType;

}
