package me.zohar.lottery.game.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 游戏
 * 
 * @author zohar
 * @date 2019年1月25日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "game", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Game {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 游戏代码
	 */
	private String gameCode;

	/**
	 * 游戏名称
	 */
	private String gameName;
	
	/**
	 * 游戏说明
	 */
	private String gameDesc;

	/**
	 * 状态,启用:1;禁用:0
	 */
	private String state;

	/**
	 * 排序号
	 */
	private Double orderNo;
	
}
