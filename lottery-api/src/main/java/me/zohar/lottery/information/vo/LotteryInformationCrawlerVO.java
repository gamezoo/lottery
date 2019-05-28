package me.zohar.lottery.information.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.information.domain.InformationCrawler;

@Data
public class LotteryInformationCrawlerVO {

	private String id;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 脚本
	 */
	private String script;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	public static List<LotteryInformationCrawlerVO> convertFor(
			List<InformationCrawler> lotteryInformationCrawlers) {
		if (CollectionUtil.isEmpty(lotteryInformationCrawlers)) {
			return new ArrayList<>();
		}
		List<LotteryInformationCrawlerVO> vos = new ArrayList<>();
		for (InformationCrawler lotteryInformationCrawler : lotteryInformationCrawlers) {
			vos.add(convertFor(lotteryInformationCrawler));
		}
		return vos;
	}

	public static LotteryInformationCrawlerVO convertFor(InformationCrawler lotteryInformationCrawler) {
		if (lotteryInformationCrawler == null) {
			return null;
		}
		LotteryInformationCrawlerVO vo = new LotteryInformationCrawlerVO();
		BeanUtils.copyProperties(lotteryInformationCrawler, vo);
		return vo;
	}

}
