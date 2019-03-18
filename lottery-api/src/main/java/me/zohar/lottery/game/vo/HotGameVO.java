package me.zohar.lottery.game.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.game.domain.HotGame;

@Data
public class HotGameVO {

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
	 * 总期数
	 */
	private Long issueCount;

	/**
	 * 当前期
	 */
	private Long currentIssue;
	
	private Long currentIssueInner;

	public static List<HotGameVO> convertFor(Collection<HotGame> hotGames) {
		if (CollectionUtil.isEmpty(hotGames)) {
			return new ArrayList<>();
		}
		List<HotGameVO> vos = new ArrayList<>();
		for (HotGame hotGame : hotGames) {
			vos.add(convertFor(hotGame));
		}
		return vos;
	}

	public static HotGameVO convertFor(HotGame hotGame) {
		if (hotGame == null) {
			return null;
		}
		HotGameVO vo = new HotGameVO();
		BeanUtils.copyProperties(hotGame, vo);
		return vo;
	}

}
