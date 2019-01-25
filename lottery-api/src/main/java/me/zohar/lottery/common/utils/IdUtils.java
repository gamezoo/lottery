package me.zohar.lottery.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class IdUtils {

	private static final Snowflake snowflake = IdUtil.createSnowflake(0, 0);

	/**
	 * Snowflake ID
	 * 
	 * @return
	 */
	public static String getId() {
		return snowflake.nextId() + "";
	}

}
