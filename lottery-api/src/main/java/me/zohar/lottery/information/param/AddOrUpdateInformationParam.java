package me.zohar.lottery.information.param;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.information.domain.LotteryInformation;

@Data
public class AddOrUpdateInformationParam {

	private String id;

	private String title;

	private String content;

	private String source;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date publishTime;
	
	public LotteryInformation convertToPo() {
		LotteryInformation po = new LotteryInformation();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
