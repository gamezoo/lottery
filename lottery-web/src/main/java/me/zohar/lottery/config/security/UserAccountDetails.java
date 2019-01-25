package me.zohar.lottery.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import me.zohar.lottery.useraccount.domain.UserAccount;

public class UserAccountDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String userName;

	private String loginPwd;

	public UserAccountDetails(UserAccount userAccount) {
		if (userAccount != null) {
			this.id = userAccount.getId();
			this.userName = userAccount.getUserName();
			this.loginPwd = userAccount.getLoginPwd();
		}
	}

	/**
	 * 获取登陆用户账号id
	 * 
	 * @return
	 */
	public String getUserAccountId() {
		return this.id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.loginPwd;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
