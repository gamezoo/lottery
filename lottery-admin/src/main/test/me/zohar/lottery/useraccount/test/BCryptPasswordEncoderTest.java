package me.zohar.lottery.useraccount.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTest {

	@Test
	public void test() {
		String encodePwd = "$2a$10$YDJLcsO4vJkSc1Y7J/ZqkO/2fM7x2wdDWWpRtU6Qu6GirDFAGtyT.";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.matches("123456", encodePwd));
	}

}
