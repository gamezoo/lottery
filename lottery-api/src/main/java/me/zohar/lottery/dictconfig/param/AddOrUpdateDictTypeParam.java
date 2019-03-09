package me.zohar.lottery.dictconfig.param;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.dictconfig.domain.DictType;

@Data
public class AddOrUpdateDictTypeParam {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 字典类型code
	 */
	@NotBlank
	private String dictTypeCode;

	/**
	 * 字典类型名称
	 */
	@NotBlank
	private String dictTypeName;

	/**
	 * 备注
	 */
	private String note;
	
	public DictType convertToPo() {
		DictType po = new DictType();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
