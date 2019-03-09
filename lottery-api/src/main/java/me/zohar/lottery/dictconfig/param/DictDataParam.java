package me.zohar.lottery.dictconfig.param;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.dictconfig.domain.DictItem;

@Data
public class DictDataParam {

	/**
	 * 字典项code
	 */
	@NotBlank
	private String dictItemCode;

	/**
	 * 字典项名称
	 */
	@NotBlank
	private String dictItemName;

	public DictItem convertToPo() {
		DictItem po = new DictItem();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
