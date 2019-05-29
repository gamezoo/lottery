package me.zohar.lottery.information.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.information.domain.LotteryInformation;

@Data
public class LotteryInformationVO {

	private String id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 发布时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date publishTime;
	
	public LotteryInformation convertToPo() {
		LotteryInformation po = new LotteryInformation();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

	public static List<LotteryInformationVO> convertFor(List<LotteryInformation> lotteryInformations) {
		if (CollectionUtil.isEmpty(lotteryInformations)) {
			return new ArrayList<>();
		}
		List<LotteryInformationVO> vos = new ArrayList<>();
		for (LotteryInformation lotteryInformation : lotteryInformations) {
			vos.add(convertFor(lotteryInformation));
		}
		return vos;
	}

	public static LotteryInformationVO convertFor(LotteryInformation lotteryInformation) {
		if (lotteryInformation == null) {
			return null;
		}
		LotteryInformationVO vo = new LotteryInformationVO();
		BeanUtils.copyProperties(lotteryInformation, vo);
		return vo;
	}

}
