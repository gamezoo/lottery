package me.zohar.lottery.game.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game_category")
@DynamicInsert(true)
@DynamicUpdate(true)
public class GameCategory {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 游戏类别code
	 */
	private String gameCategoryCode;

	/**
	 * 游戏类别
	 */
	private String gameCategoryName;

	/**
	 * 排序号
	 */
	private Double orderNo;

	/**
	 * 游戏集合
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_category_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("orderNo ASC")
	private Set<Game> games;

}
