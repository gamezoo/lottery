package me.zohar.lottery.game.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 号位
 * 
 * @author zohar
 * @date 2019年1月10日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "num_locate", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class NumLocate {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 号位名称
	 */
	private String numLocateName;

	/**
	 * 候选的号码集合,以逗号分隔
	 */
	private String nums;

	/**
	 * 最大可选多少个号码
	 */
	private Integer maxSelected;

	/**
	 * 是否有过滤按钮
	 */
	private Boolean hasFilterBtnFlag;

	/**
	 * 排序号
	 */
	private Double orderNo;
	
	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

	/**
	 * 对应游戏玩法id
	 */
	@Column(name = "game_play_id", length = 32)
	private String gamePlayId;

}
