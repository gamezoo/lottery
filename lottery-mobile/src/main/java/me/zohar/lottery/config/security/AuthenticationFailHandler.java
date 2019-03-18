package me.zohar.lottery.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import me.zohar.lottery.common.vo.Result;

/**
 * 登录失败处理类
 * 
 * @author zohar
 * @date 2019年1月23日
 *
 */
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(JSONObject.toJSONString(Result.fail(500, "用户名或密码错误")));
		out.flush();
		out.close();
	}

}
