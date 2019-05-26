package me.zohar.lottery.systemnotice.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemNoticeQueryCondParam extends PageParam {

	private String noticeTitle;

}
