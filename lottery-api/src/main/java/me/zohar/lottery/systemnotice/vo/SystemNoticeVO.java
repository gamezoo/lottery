package me.zohar.lottery.systemnotice.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.systemnotice.domain.SystemNotice;

@Data
public class SystemNoticeVO {

	private String id;

	private String noticeTitle;

	private String noticeContent;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 发布日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date publishDate;

	public static List<SystemNoticeVO> convertFor(List<SystemNotice> systemNotices) {
		if (CollectionUtil.isEmpty(systemNotices)) {
			return new ArrayList<>();
		}
		List<SystemNoticeVO> vos = new ArrayList<>();
		for (SystemNotice systemNotice : systemNotices) {
			vos.add(convertFor(systemNotice));
		}
		return vos;
	}

	public static SystemNoticeVO convertFor(SystemNotice systemNotice) {
		if (systemNotice == null) {
			return null;
		}
		SystemNoticeVO vo = new SystemNoticeVO();
		BeanUtils.copyProperties(systemNotice, vo);
		return vo;
	}
}
