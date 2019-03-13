package me.zohar;

import java.text.MessageFormat;

import org.junit.Test;

public class SpelTest {

	@Test
	public void test() {
		System.out.println(MessageFormat.format("充值返水率:{0}%", 5));
	}

}
