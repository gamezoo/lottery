package me.zohar.game;

import org.junit.Test;
import org.springframework.util.DigestUtils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;

public class BettingCountTest {

	@Test
	public void test() {
		String startTime = "07:10";
		DateTime dateTime = DateUtil.parse(startTime, "hh:mm");
		System.out.println(dateTime);
		System.out.println(dateTime.hour(true));
	}

}
