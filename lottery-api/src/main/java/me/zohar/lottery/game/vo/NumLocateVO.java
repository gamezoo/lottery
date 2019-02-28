package me.zohar.lottery.game.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.game.domain.NumLocate;

@Data
public class NumLocateVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 号位名称
	 */
	private String numLocateName;

	/**
	 * 候选的号码集合,以逗号分隔
	 */
	private String nums;

	/**
	 * 最大可选多少个号码
	 */
	private Integer maxSelected;

	/**
	 * 是否有过滤按钮
	 */
	private Boolean hasFilterBtnFlag;

	/**
	 * 对应游戏玩法id
	 */
	private String gamePlayId;
	
	/**
	 * 候选号码集合
	 */
	private List<OptionalNumVO> optionalNums = new ArrayList<>();

	public static List<NumLocateVO> convertFor(Collection<NumLocate> numLocates) {
		if (CollectionUtil.isEmpty(numLocates)) {
			return new ArrayList<>();
		}
		List<NumLocateVO> vos = new ArrayList<>();
		for (NumLocate numLocate : numLocates) {
			vos.add(convertFor(numLocate));
		}
		return vos;
	}

	public static NumLocateVO convertFor(NumLocate numLocate) {
		if (numLocate == null) {
			return null;
		}
		NumLocateVO vo = new NumLocateVO();
		BeanUtils.copyProperties(numLocate, vo);
		return vo;
	}

}
