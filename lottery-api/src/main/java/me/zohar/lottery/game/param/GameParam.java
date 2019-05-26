package me.zohar.lottery.game.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.game.domain.Game;

@Data
public class GameParam {

	/**
	 * 主键id
	 */
	private String id;
	
	/**
	 * 游戏类别id
	 */
	@NotBlank
	private String gameCategoryId;

	/**
	 * 游戏代码
	 */
	@NotBlank
	private String gameCode;

	/**
	 * 游戏名称
	 */
	@NotBlank
	private String gameName;
	
	/**
	 * 游戏说明
	 */
	private String gameDesc;
	
	@NotNull
	private Boolean hotGameFlag;

	/**
	 * 状态,启用:1;禁用:0
	 */
	@NotBlank
	private String state;

	/**
	 * 排序号
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = true, message = "orderNo不能少于0")
	private Double orderNo;

	/**
	 * 复制的游戏代码
	 */
	private String copyGameCode;
	
	public Game convertToPo() {
		Game po = new Game();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
