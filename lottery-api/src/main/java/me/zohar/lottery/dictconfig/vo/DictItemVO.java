package me.zohar.lottery.dictconfig.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.dictconfig.domain.DictItem;

@Data
public class DictItemVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	public static List<DictItemVO> convertFor(List<DictItem> dictItems) {
		if (CollectionUtil.isEmpty(dictItems)) {
			return new ArrayList<>();
		}
		List<DictItemVO> vos = new ArrayList<>();
		for (DictItem dictItem : dictItems) {
			vos.add(convertFor(dictItem));
		}
		return vos;
	}

	public static DictItemVO convertFor(DictItem dictItem) {
		if (dictItem == null) {
			return null;
		}
		DictItemVO vo = new DictItemVO();
		BeanUtils.copyProperties(dictItem, vo);
		return vo;
	}

}
