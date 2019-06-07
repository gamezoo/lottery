package me.zohar.lottery.agent.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.agent.domain.RebateAndOddsSituation;

@Data
public class RebateAndOddsSituationVO {
	
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
	 * 关联账号数量
	 */
	private Integer associatedAccountNum;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	public static List<RebateAndOddsSituationVO> convertFor(List<RebateAndOddsSituation> rebateAndOddsSituations) {
		if (CollectionUtil.isEmpty(rebateAndOddsSituations)) {
			return new ArrayList<>();
		}
		List<RebateAndOddsSituationVO> vos = new ArrayList<>();
		for (RebateAndOddsSituation rebateAndOddsSituation : rebateAndOddsSituations) {
			vos.add(convertFor(rebateAndOddsSituation));
		}
		return vos;
	}

	public static RebateAndOddsSituationVO convertFor(RebateAndOddsSituation rebateAndOddsSituation) {
		if (rebateAndOddsSituation == null) {
			return null;
		}
		RebateAndOddsSituationVO vo = new RebateAndOddsSituationVO();
		BeanUtils.copyProperties(rebateAndOddsSituation, vo);
		return vo;
	}

}
