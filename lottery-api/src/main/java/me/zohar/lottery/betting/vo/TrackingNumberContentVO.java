package me.zohar.lottery.betting.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.betting.domain.TrackingNumberContent;
import me.zohar.lottery.dictconfig.DictHolder;

@Data
public class TrackingNumberContentVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 游戏玩法代码
	 */
	private String gamePlayCode;

	/**
	 * 游戏玩法名称
	 */
	private String gamePlayName;

	/**
	 * 所选号码
	 */
	private String selectedNo;

	/**
	 * 注数
	 */
	private Long bettingCount;

	public static List<TrackingNumberContentVO> convertFor(Collection<TrackingNumberContent> trackingNumberContents) {
		if (CollectionUtil.isEmpty(trackingNumberContents)) {
			return new ArrayList<>();
		}
		List<TrackingNumberContentVO> vos = new ArrayList<>();
		for (TrackingNumberContent trackingNumberContent : trackingNumberContents) {
			vos.add(convertFor(trackingNumberContent));
		}
		return vos;
	}

	public static TrackingNumberContentVO convertFor(TrackingNumberContent trackingNumberContent) {
		if (trackingNumberContent == null) {
			return null;
		}
		TrackingNumberContentVO vo = new TrackingNumberContentVO();
		BeanUtils.copyProperties(trackingNumberContent, vo);
		vo.setGamePlayName(
				DictHolder.getDictItemName("gamePlay", trackingNumberContent.getTrackingNumberOrder().getGameCode()
						+ "_" + trackingNumberContent.getGamePlayCode()));
		return vo;
	}

}
