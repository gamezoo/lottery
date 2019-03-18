package me.zohar.lottery.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.useraccount.service.UserAccountService;

/**
 * 登录成功处理类
 * 
 * @author zohar
 * @date 2019年1月23日
 *
 */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private UserAccountService userAccountService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserAccountDetails user = (UserAccountDetails) authentication.getPrincipal();
		userAccountService.updateLatelyLoginTime(user.getUserAccountId());

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(JSONObject.toJSONString(Result.success().setMsg("登录成功")));
		out.flush();
		out.close();
	}
}
