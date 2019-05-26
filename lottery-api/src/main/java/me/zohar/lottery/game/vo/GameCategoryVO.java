package me.zohar.lottery.game.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.game.domain.GameCategory;

@Data
public class GameCategoryVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 游戏类别code
	 */
	private String gameCategoryCode;

	/**
	 * 游戏类别
	 */
	private String gameCategoryName;

	/**
	 * 排序号
	 */
	private Double orderNo;

	public static List<GameCategoryVO> convertFor(Collection<GameCategory> gameCategorys) {
		if (CollectionUtil.isEmpty(gameCategorys)) {
			return new ArrayList<>();
		}
		List<GameCategoryVO> vos = new ArrayList<>();
		for (GameCategory gameCategory : gameCategorys) {
			vos.add(convertFor(gameCategory));
		}
		return vos;
	}

	public static GameCategoryVO convertFor(GameCategory gameCategory) {
		if (gameCategory == null) {
			return null;
		}
		GameCategoryVO vo = new GameCategoryVO();
		BeanUtils.copyProperties(gameCategory, vo);
		return vo;
	}

}
