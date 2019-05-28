package me.zohar.lottery.information.param;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.information.domain.InformationCrawler;

@Data
public class AddOrUpdateInformationCrawlerParam {

	private String id;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 脚本
	 */
	private String script;

	public InformationCrawler convertToPo() {
		InformationCrawler po = new InformationCrawler();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		return po;
	}

}
