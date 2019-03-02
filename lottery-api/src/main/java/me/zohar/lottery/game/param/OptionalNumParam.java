package me.zohar.lottery.game.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.game.domain.OptionalNum;

@Data
public class OptionalNumParam {

	@NotBlank
	private String num;

	@DecimalMin(value = "0", inclusive = false)
	private Double odds;
	
	public OptionalNum convertToPo() {
		OptionalNum po = new OptionalNum();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
