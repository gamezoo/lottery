package me.zohar.lottery.dictconfig.param;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateDictDataParam {

	@NotBlank
	private String dictTypeId;

	@Valid
	private List<DictDataParam> dictDatas;

}
