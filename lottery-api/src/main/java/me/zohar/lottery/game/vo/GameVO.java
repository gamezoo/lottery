package me.zohar.lottery.game.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.game.domain.Game;

@Data
public class GameVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 状态
	 */
	private String state;
	
	private String stateName;

	/**
	 * 排序号
	 */
	private Double orderNo;

	public static List<GameVO> convertFor(Collection<Game> games) {
		if (CollectionUtil.isEmpty(games)) {
			return new ArrayList<>();
		}
		List<GameVO> vos = new ArrayList<>();
		for (Game game : games) {
			vos.add(convertFor(game));
		}
		return vos;
	}

	public static GameVO convertFor(Game game) {
		if (game == null) {
			return null;
		}
		GameVO vo = new GameVO();
		BeanUtils.copyProperties(game, vo);
		vo.setStateName(DictHolder.getDictItemName("gameState", vo.getState()));
		return vo;
	}

}
