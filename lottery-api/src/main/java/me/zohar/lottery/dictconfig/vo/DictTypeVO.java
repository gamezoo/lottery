package me.zohar.lottery.dictconfig.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.dictconfig.domain.DictType;

@Data
public class DictTypeVO {

	/**
	 * 主键id
	 */
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

	public static List<DictTypeVO> convertFor(List<DictType> dictTypes) {
		if (CollectionUtil.isEmpty(dictTypes)) {
			return new ArrayList<>();
		}
		List<DictTypeVO> vos = new ArrayList<>();
		for (DictType dictType : dictTypes) {
			vos.add(convertFor(dictType));
		}
		return vos;
	}

	public static DictTypeVO convertFor(DictType dictType) {
		if (dictType == null) {
			return null;
		}
		DictTypeVO vo = new DictTypeVO();
		BeanUtils.copyProperties(dictType, vo);
		return vo;
	}

}
