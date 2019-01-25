package me.zohar.lottery.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;

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
	private UserAccountRepo userAccountRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = userAccountRepo.findByUserName(username);
		if (userAccount == null) {
			log.warn("账号不存在:{}", username);
			throw new UsernameNotFoundException("用户名或密码不正确");
		}

		return new UserAccountDetails(userAccount);
	}

}
