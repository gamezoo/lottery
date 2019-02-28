package me.zohar.lottery.game.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.game.domain.OptionalNum;

@Data
public class OptionalNumVO {

	/**
	 * 主键id
	 */
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
	 * 对应号位id
	 */
	private String numLocateId;

	public static List<OptionalNumVO> convertFor(Collection<OptionalNum> optionalNums) {
		if (CollectionUtil.isEmpty(optionalNums)) {
			return new ArrayList<>();
		}
		List<OptionalNumVO> vos = new ArrayList<>();
		for (OptionalNum optionalNum : optionalNums) {
			vos.add(convertFor(optionalNum));
		}
		return vos;
	}

	public static OptionalNumVO convertFor(OptionalNum optionalNum) {
		if (optionalNum == null) {
			return null;
		}
		OptionalNumVO vo = new OptionalNumVO();
		BeanUtils.copyProperties(optionalNum, vo);
		return vo;
	}

}
