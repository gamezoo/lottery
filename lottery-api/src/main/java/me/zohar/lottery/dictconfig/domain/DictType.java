package me.zohar.lottery.dictconfig.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典类型
 * 
 * @author zohar
 * @date 2019年1月19日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "dict_type", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class DictType {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 字典类型code
	 */
	private String dictTypeCode;

	/**
	 * 字典类型名称
	 */
	private String dictTypeName;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "dict_type_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("orderNo ASC")
	private Set<DictItem> dictItems;

}
