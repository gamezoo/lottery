package me.zohar.lottery.agent.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.agent.domain.RebateAndOdds;

@Data
public class RebateAndOddsVO {

	private String id;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 赔率
	 */
	private Double odds;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	public static List<RebateAndOddsVO> convertFor(List<RebateAndOdds> rebateAndOddses) {
		if (CollectionUtil.isEmpty(rebateAndOddses)) {
			return new ArrayList<>();
		}
		List<RebateAndOddsVO> vos = new ArrayList<>();
		for (RebateAndOdds rebateAndOdds : rebateAndOddses) {
			vos.add(convertFor(rebateAndOdds));
		}
		return vos;
	}

	public static RebateAndOddsVO convertFor(RebateAndOdds rebateAndOdds) {
		if (rebateAndOdds == null) {
			return null;
		}
		RebateAndOddsVO vo = new RebateAndOddsVO();
		BeanUtils.copyProperties(rebateAndOdds, vo);
		return vo;
	}

}
