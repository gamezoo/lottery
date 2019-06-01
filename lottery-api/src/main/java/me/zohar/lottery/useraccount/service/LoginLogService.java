package me.zohar.lottery.useraccount.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgent;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.common.utils.ThreadPoolUtils;
import me.zohar.lottery.useraccount.domain.LoginLog;
import me.zohar.lottery.useraccount.repo.LoginLogRepo;

@Slf4j
@Service
public class LoginLogService {

	public static final String 淘宝IP查询地址 = "http://ip.taobao.com/service/getIpInfo.php";

	@Autowired
	private LoginLogRepo loginLogRepo;

	@Transactional
	public void recordLoginLog(String userName, String state, String msg, String ipAddr, UserAgent userAgent) {
		ThreadPoolUtils.getLoginLogPool().schedule(() -> {
			Date now = new Date();
			String loginLocation = null;
			try {
				String respResult = HttpUtil.get(淘宝IP查询地址 + "?ip=" + ipAddr);
				JSONObject jsonObject = JSONObject.parseObject(respResult);
				loginLocation = jsonObject.getJSONObject("data").getString("region")
						+ jsonObject.getJSONObject("data").getString("city");
			} catch (Exception e) {
				log.error("ip查询异常", e);
			}
			LoginLog loginLog = new LoginLog();
			loginLog.setId(IdUtils.getId());
			loginLog.setUserName(userName);
			loginLog.setState(state);
			loginLog.setMsg(msg);
			loginLog.setIpAddr(ipAddr);
			loginLog.setLoginLocation(loginLocation);
			loginLog.setLoginTime(now);
			loginLog.setBrowser(userAgent.getBrowser().getName());
			loginLog.setOs(userAgent.getOs().getName());
			loginLogRepo.save(loginLog);

		}, 10, TimeUnit.MILLISECONDS);
	}

}
