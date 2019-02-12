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
	 * 游戏代码
	 */
	@NotBlank(message = "gameCode不能为空")
	private String gameCode;

	/**
	 * 游戏名称
	 */
	@NotBlank(message = "gameName不能为空")
	private String gameName;
	
	/**
	 * 游戏说明
	 */
	private String gameDesc;

	/**
	 * 状态,启用:1;禁用:0
	 */
	@NotBlank(message = "state不能为空")
	private String state;

	/**
	 * 排序号
	 */
	@NotNull(message = "orderNo不能为空")
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
