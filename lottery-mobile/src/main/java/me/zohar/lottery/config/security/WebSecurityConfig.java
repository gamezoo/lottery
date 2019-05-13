package me.zohar.lottery.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private AuthenticationSuccessHandler successHandler;

	@Autowired
	private AuthenticationFailHandler failHandler;

	@Autowired
	private LogoutHandler logoutHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/register").permitAll()
		.antMatchers("/game-hall").permitAll()
		.antMatchers("/lottery-hall").permitAll()
		.antMatchers("/my-home-page").permitAll()
		.antMatchers("/game/findTop5HotGame").permitAll()
		.antMatchers("/game/findAllGameSituation").permitAll()
		.antMatchers("/masterControl/getInviteRegisterSetting").permitAll()
		.antMatchers("/userAccount/register").permitAll()
		.antMatchers("/userAccount/getUserAccountInfo").permitAll()
		.anyRequest().authenticated()
		.and().formLogin().loginPage("/login").loginProcessingUrl("/login")
		.successHandler(successHandler).failureHandler(failHandler).permitAll()
		.and().logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandler).permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/plugins/**");
	}

	/**
	 * 添加 UserDetailsService， 实现自定义登录校验
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.eraseCredentials(false).userDetailsService(customUserDetailsService)
				.passwordEncoder(new BCryptPasswordEncoder());
	}

}
