package me.zohar;

import java.util.Date;

import org.junit.Test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

public class SpelTest {

	@Test
	public void test() {
		DateTime offset = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 2);
		System.out.println(offset);
	}

}
