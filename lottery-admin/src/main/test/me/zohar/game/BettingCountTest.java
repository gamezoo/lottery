package me.zohar.game;

import org.junit.Test;
import org.springframework.util.DigestUtils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;

public class BettingCountTest {

	@Test
	public void test() {
		Double double1 = 0.01;
		Double double2 = 0.02;
		System.out.println(double1.compareTo(double2));
	}

}
