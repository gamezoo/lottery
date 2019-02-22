package me.zohar.lottery.useraccount.domain;

import java.util.Date;

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
 * 用户账号
 * 
 * @author zohar
 * @date 2018年12月26日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "user_account", schema = "lottery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class UserAccount {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 登录密码
	 */
	private String loginPwd;

	/**
	 * 资金密码
	 */
	private String moneyPwd;

	/**
	 * 余额
	 */
	private Double balance;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 注册时间
	 */
	private Date registeredTime;

	/**
	 * 最近登录时间
	 */
	private Date latelyLoginTime;

	/**
	 * 开户银行
	 */
	private String openAccountBank;

	/**
	 * 开户人姓名
	 */
	private String accountHolder;

	/**
	 * 银行卡账号
	 */
	private String bankCardAccount;

	/**
	 * 银行资料最近修改时间
	 */
	private Date bankInfoLatelyModifyTime;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

}
