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
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 游戏玩法
 * 
 * @author zohar
 * @date 2019年1月10日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "game_play", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class GamePlay {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 游戏玩法代码
	 */
	private String gamePlayCode;

	/**
	 * 游戏玩法名称
	 */
	private String gamePlayName;

	/**
	 * 赔率
	 */
	private Double odds;

	/**
	 * 赔率模式
	 */
	private String oddsMode;

	/**
	 * 玩法描述
	 */
	@Column(length = 1024)
	private String gamePlayDesc;

	/**
	 * 排序号
	 */
	private Double orderNo;

	/**
	 * 所属游戏代码
	 */
	private String gameCode;

	/**
	 * 状态,启用:1;禁用:0
	 */
	private String state;

	/**
	 * 所属游戏玩法类别名称
	 */
	private String gamePlayCategoryName;

	/**
	 * 所属游戏子玩法类别名称
	 */
	private String subGamePlayCategoryName;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	/**
	 * 号位集合
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_play_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("orderNo ASC")
	private Set<NumLocate> numLocates;

}
