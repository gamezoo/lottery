package me.zohar.lottery.game.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.game.domain.NumLocate;

@Data
public class NumLocateParam {
	
	/**
	 * 号位名称
	 */
	@NotBlank(message = "numLocateName不能为空")
	private String numLocateName;

	/**
	 * 候选的号码集合,以逗号分隔
	 */
	@NotBlank(message = "nums不能为空")
	private String nums;

	/**
	 * 最大可选多少个号码
	 */
	@DecimalMin(value = "1", inclusive = true, message = "maxSelected不能小于1")
	private Integer maxSelected;

	/**
	 * 是否有过滤按钮
	 */
	@NotNull(message = "hasFilterBtnFlag不能为空")
	private Boolean hasFilterBtnFlag;
	
	public NumLocate convertToPo() {
		NumLocate po = new NumLocate();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
