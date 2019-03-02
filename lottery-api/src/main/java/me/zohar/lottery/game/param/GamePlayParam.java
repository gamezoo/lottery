package me.zohar.lottery.game.param;

import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.game.domain.GamePlay;

@Data
public class GamePlayParam {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 游戏玩法代码
	 */
	@NotBlank
	private String gamePlayCode;

	/**
	 * 游戏玩法名称
	 */
	@NotBlank
	private String gamePlayName;
	
	/**
	 * 赔率模式
	 */
	@NotNull
	private String oddsMode;

	/**
	 * 赔率
	 */
	private Double odds;

	/**
	 * 玩法描述
	 */
	@NotBlank
	private String gamePlayDesc;

	/**
	 * 排序号
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = true, message = "orderNo不能少于0")
	private Double orderNo;

	/**
	 * 所属游戏代码
	 */
	@NotBlank
	private String gameCode;

	/**
	 * 状态,启用:1;禁用:0
	 */
	@NotBlank
	private String state;

	/**
	 * 所属游戏玩法类别名称
	 */
	@NotBlank
	private String gamePlayCategoryName;

	/**
	 * 所属游戏子玩法类别名称
	 */
	@NotBlank
	private String subGamePlayCategoryName;

	/**
	 * 号位集合
	 */
	@NotEmpty
	private List<NumLocateParam> numLocates;

	public GamePlay convertToPo() {
		GamePlay po = new GamePlay();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
