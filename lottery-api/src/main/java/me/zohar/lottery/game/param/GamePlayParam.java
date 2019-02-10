package me.zohar.lottery.game.param;

import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
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
	@NotBlank(message = "gamePlayCode不能为空")
	private String gamePlayCode;

	/**
	 * 游戏玩法名称
	 */
	@NotBlank(message = "gamePlayName不能为空")
	private String gamePlayName;

	/**
	 * 赔率
	 */
	@NotNull(message = "odds不能为空")
	@DecimalMin(value = "0", inclusive = false, message = "odds不能少于或等于0")
	private Double odds;

	/**
	 * 玩法描述
	 */
	@NotBlank(message = "gamePlayDesc不能为空")
	private String gamePlayDesc;
	
	/**
	 * 排序号
	 */
	@NotNull(message = "orderNo不能为空")
	@DecimalMin(value = "0", inclusive = true, message = "orderNo不能少于0")
	private Double orderNo;

	/**
	 * 所属游戏代码
	 */
	@NotBlank(message = "gameCode不能为空")
	private String gameCode;

	/**
	 * 状态,启用:1;禁用:0
	 */
	@NotBlank(message = "state不能为空")
	private String state;
	
	/**
	 * 所属游戏玩法类别名称
	 */
	@NotBlank(message = "gamePlayCategoryName不能为空")
	private String gamePlayCategoryName;

	/**
	 * 所属游戏子玩法类别名称
	 */
	@NotBlank(message = "subGamePlayCategoryName不能为空")
	private String subGamePlayCategoryName;
	
	/**
	 * 号位集合
	 */
	private List<NumLocateParam> numLocates;
	
	public GamePlay convertToPo() {
		GamePlay po = new GamePlay();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
