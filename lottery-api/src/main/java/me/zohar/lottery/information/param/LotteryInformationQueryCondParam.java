package me.zohar.lottery.information.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryInformationQueryCondParam extends PageParam {

	private String title;

	private String source;
	
	

}
