package me.zohar.lottery.systemnotice.param;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.systemnotice.domain.SystemNotice;

@Data
public class AddOrUpdateSystemNoticeParam {

	private String id;

	private String noticeTitle;

	private String noticeContent;

	/**
	 * 发布日期
	 */
	private String publishDate;

	public SystemNotice convertToPo() {
		SystemNotice po = new SystemNotice();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		po.setPublishDate(DateUtil.parse(this.getPublishDate(), DatePattern.NORM_DATE_PATTERN));
		return po;
	}

}
