package me.zohar.lottery.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.useraccount.service.UserAccountService;
import me.zohar.lottery.useraccount.vo.LoginAccountInfoVO;

/**
 * 通过实现UserDetailsService接口提供复杂认证
 * 
 * @author 黄振华
 * @date 2018年8月28日
 *
 */
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserAccountService userAccountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginAccountInfoVO loginAccountInfo = userAccountService.getLoginAccountInfo(username);
		if (loginAccountInfo == null) {
			log.warn("账号不存在:{}", username);
			throw new UsernameNotFoundException("用户名或密码不正确");
		}
		if (!Constant.账号类型_管理员.equals(loginAccountInfo.getAccountType())) {
			log.warn("该账号不是管理员,无法登陆到后台:{}", username);
			throw new UsernameNotFoundException("该账号不是管理员,无法登陆到后台");
		}

		return new UserAccountDetails(loginAccountInfo);
	}

}
