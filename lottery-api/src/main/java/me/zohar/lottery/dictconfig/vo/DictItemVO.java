package me.zohar.lottery.dictconfig.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.dictconfig.domain.DictItem;

@Data
public class DictItemVO {

	/**
	 * 主键id
	 */
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
	
	public static DictItemVO convertFor(DictItem dictItem) {
		if (dictItem == null) {
			return null;
		}
		DictItemVO vo = new DictItemVO();
		BeanUtils.copyProperties(dictItem, vo);
		return vo;
	}

}
