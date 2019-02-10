package me.zohar.lottery.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import me.zohar.lottery.common.vo.Result;

/**
 * 退出登录处理类
 * 
 * @author zohar
 * @date 2019年1月23日
 *
 */
@Component
public class LogoutHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		System.out.println("logout....");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(JSONObject.toJSONString(Result.success().setMsg("退出成功")));
		out.flush();
		out.close();

	}

}
