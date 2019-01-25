package me.zohar.lottery.common.utils;

/**
 * 号球工具类
 * 
 * @author zohar
 * @date 2019年1月12日
 *
 */
public class BallUtils {

	public static int[][] buildSSCBall(String selectedNo) {
		String[] split = selectedNo.split(",");
		int[][] balls = new int[5][];
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			if ("-".equals(s)) {
				balls[i] = new int[0];
			} else {
				balls[i] = new int[s.length()];
				for (int j = 0; j < s.length(); j++) {
					balls[i][j] = Integer.parseInt(String.valueOf(s.charAt(j)));
				}
			}
		}
		return balls;
	}

}
