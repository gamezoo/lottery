package me.zohar.lottery.issue.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zohar.lottery.game.domain.Game;

/**
 * 期号设置
 * 
 * @author zohar
 * @date 2019年2月13日
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "issue_setting", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class IssueSetting {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 日期格式
	 */
	private String dateFormat;

	/**
	 * 期号格式
	 */
	private String issueFormat;
	
	/**
	 * 对应游戏id
	 */
	@Column(name = "game_id", length = 32)
	private String gameId;
	
	/**
	 * 对应游戏
	 */
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Game game;
	
	/**
	 * 期号生成规则集合
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_setting_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@OrderBy("orderNo ASC")
	private Set<IssueGenerateRule> issueGenerateRules;

	public void updateFormat(String dateFormat, String issueFormat) {
		this.setDateFormat(dateFormat);
		this.setIssueFormat(issueFormat);
	}

}
