package me.zohar.lottery.issue.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.zohar.lottery.common.utils.BallUtils;

/**
 * 游戏玩法枚举类
 * 
 * @author zohar
 * @date 2018年12月26日
 *
 */
@Getter
@AllArgsConstructor
public enum GamePlayEnum {

	重庆时时彩_五星定位("CQSSC_5XDW", "重庆时时彩_五星定位") {

		/**
		 * 在万位、千位、百位、十位、个位上的任意位置，选择1个号码作为一注，选号与相同位置上的开奖号码一致，即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：7**** 投注号码：7****
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			for (int i = 0; i < lotteryNums.length; i++) {
				if (selectedNos[i].contains(lotteryNums[i])) {
					winningCount++;
				}
			}
			return winningCount;
		}

	},

	重庆时时彩_后一直选("CQSSC_H1ZX", "重庆时时彩_后一直选") {

		/**
		 * 选择1个号码作为一注，选号与个位上的开奖号码一致，即中奖。 <br/>
		 * 中奖实例:<br/>
		 * 开奖号码：****7 投注号码：****7
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");

			String lastLotteryNum = lotteryNums[lotteryNums.length - 1];
			if (selectedNo.contains(lastLotteryNum)) {
				winningCount++;
			}
			return winningCount;
		}

	},

	重庆时时彩_五星直选("CQSSC_5XZX", "重庆时时彩_五星直选") {

		/**
		 * 在万位、千位、百位、十位、个位上，分别选择一个号码，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：55569 投注号码：55569
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			if (selectedNos[0].contains(lotteryNums[0]) && selectedNos[1].contains(lotteryNums[1])
					&& selectedNos[2].contains(lotteryNums[2]) && selectedNos[3].contains(lotteryNums[3])
					&& selectedNos[4].contains(lotteryNums[4])) {
				winningCount = 1;
			}
			return winningCount;
		}
	},

	重庆时时彩_前四直选("CQSSC_Q4ZX", "重庆时时彩_前四直选") {

		/**
		 * 在万位、千位、百位、十位上，分别选择一个号码，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：5556* 投注号码：5556*
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			if (selectedNos[0].contains(lotteryNums[0]) && selectedNos[1].contains(lotteryNums[1])
					&& selectedNos[2].contains(lotteryNums[2]) && selectedNos[3].contains(lotteryNums[3])) {
				winningCount = 1;
			}
			return winningCount;
		}
	},

	重庆时时彩_后四直选("CQSSC_H4ZX", "重庆时时彩_后四直选") {

		/**
		 * 在千位、百位、十位、个位上，分别选择一个号码，号码和顺序都一致即中奖。。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：*5556 投注号码：*5556
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			if (selectedNos[0].contains(lotteryNums[1]) && selectedNos[1].contains(lotteryNums[2])
					&& selectedNos[2].contains(lotteryNums[3]) && selectedNos[3].contains(lotteryNums[4])) {
				winningCount = 1;
			}
			return winningCount;
		}
	},

	重庆时时彩_前三直选("CQSSC_Q3ZX", "重庆时时彩_前三直选") {

		/**
		 * 在万位、千位、百位上，分别选择一个号码，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：258** 投注号码：258**
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			if (selectedNos[0].contains(lotteryNums[0]) && selectedNos[1].contains(lotteryNums[1])
					&& selectedNos[2].contains(lotteryNums[2])) {
				winningCount = 1;
			}
			return winningCount;
		}
	},

	重庆时时彩_中三直选("CQSSC_Z3ZX", "重庆时时彩_中三直选") {

		/**
		 * 在千位、百位、十位上，分别选择一个号码，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：*258* 投注号码：*258*
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			if (selectedNos[0].contains(lotteryNums[1]) && selectedNos[1].contains(lotteryNums[2])
					&& selectedNos[2].contains(lotteryNums[3])) {
				winningCount = 1;
			}
			return winningCount;
		}
	},

	重庆时时彩_后三直选("CQSSC_H3ZX", "重庆时时彩_后三直选") {

		/**
		 * 在百位、十位、个位上，分别选择一个号码作为一注，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：**258 投注号码：**258
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			if (selectedNos[0].contains(lotteryNums[2]) && selectedNos[1].contains(lotteryNums[3])
					&& selectedNos[2].contains(lotteryNums[4])) {
				winningCount = 1;
			}
			return winningCount;
		}
	},

	重庆时时彩_前二直选("CQSSC_Q2ZX", "重庆时时彩_前二直选") {

		/**
		 * 在万位、千位上，分别选择一个号码，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：47*** 投注号码：47***
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			if (selectedNos[0].contains(lotteryNums[0]) && selectedNos[1].contains(lotteryNums[1])) {
				winningCount = 1;
			}
			return winningCount;
		}
	},

	重庆时时彩_前二组选("CQSSC_Q2ZUX", "前二组选") {

		/**
		 * 从0-9中选择2个数字组成一注，所选号码与开奖号码的前两位数值相同（顺序不限），即中奖。<br/>
		 * 投注号码的个数不能超过全包号码的60%<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：58*** 投注号码：5,8
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String top2LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 0; i < lotteryNums.length - 3; i++) {
				top2LotteryNum += lotteryNums[i];
			}
			List<String> selectedNoWithSplit = new ArrayList<>();
			int selectedNoCount = selectedNo.length();
			for (int c = 0; c < selectedNoCount - 1; c++) {
				for (int p = c + 1; p < selectedNoCount; p++) {
					StringBuilder sb = new StringBuilder();
					sb.append(selectedNo.charAt(c));
					sb.append(selectedNo.charAt(p));
					selectedNoWithSplit.add(sb.toString());
				}
			}
			for (String num : selectedNoWithSplit) {
				if (top2LotteryNum.contains(String.valueOf(num.charAt(0)))
						&& top2LotteryNum.contains(String.valueOf(num.charAt(1)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_后二直选("CQSSC_H2ZX", "重庆时时彩_后二直选") {

		/**
		 * 在十位、个位上，分别选择一个号码作为一注，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：***47 投注号码：***47
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			if (selectedNos[0].contains(lotteryNums[3]) && selectedNos[1].contains(lotteryNums[4])) {
				winningCount = 1;
			}
			return winningCount;
		}
	},

	重庆时时彩_后二组选("CQSSC_H2ZUX", "后二组选") {

		/**
		 * 从0-9中选择2个数字组成一注，所选号码与开奖号码的后两位数值相同（顺序不限），即中奖。<br/>
		 * 投注号码的个数不能超过全包号码的60% <br/>
		 * 中奖实例:<br/>
		 * 开奖号码：***58 投注号码：5,8
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String end2LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 3; i < lotteryNums.length - 1; i++) {
				end2LotteryNum += lotteryNums[i];
			}
			List<String> selectedNoWithSplit = new ArrayList<>();
			int selectedNoCount = selectedNo.length();
			for (int c = 0; c < selectedNoCount - 1; c++) {
				for (int p = c + 1; p < selectedNoCount; p++) {
					StringBuilder sb = new StringBuilder();
					sb.append(selectedNo.charAt(c));
					sb.append(selectedNo.charAt(p));
					selectedNoWithSplit.add(sb.toString());
				}
			}
			for (String num : selectedNoWithSplit) {
				if (end2LotteryNum.contains(String.valueOf(num.charAt(0)))
						&& end2LotteryNum.contains(String.valueOf(num.charAt(1)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_前三一码("CQSSC_Q31M", "重庆时时彩_前三一码") {

		/**
		 * 从0-9中至少选择1个号码投注，竞猜开奖号码前三位中包含这个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：7****投注号码：7
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String top3LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 0; i < lotteryNums.length - 2; i++) {
				top3LotteryNum += lotteryNums[i];
			}
			for (int i = 0; i < selectedNo.length(); i++) {
				if (top3LotteryNum.contains(String.valueOf(selectedNo.charAt(i)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_中三一码("CQSSC_Z31M", "中三一码") {

		/**
		 * 从0-9中至少选择1个号码投注，竞猜开奖号码中三位中包含这个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：**7**投注号码：7
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String middle3LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 1; i < lotteryNums.length - 1; i++) {
				middle3LotteryNum += lotteryNums[i];
			}
			for (int i = 0; i < selectedNo.length(); i++) {
				if (middle3LotteryNum.contains(String.valueOf(selectedNo.charAt(i)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_后三一码("CQSSC_H31M", "后三一码") {

		/**
		 * 从0-9中至少选择1个号码作为一注，开奖号码后三位中包含这个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：**3xx 投注号码：3
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String end3LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 2; i < lotteryNums.length; i++) {
				end3LotteryNum += lotteryNums[i];
			}
			for (int i = 0; i < selectedNo.length(); i++) {
				if (end3LotteryNum.contains(String.valueOf(selectedNo.charAt(i)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_前三二码("CQSSC_Q32M", "前三二码") {

		/**
		 * 从0-9中至少选择2个号码投注，竞猜开奖号码前三位中包含这2个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：72***投注号码：7,2
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String top3LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 0; i < lotteryNums.length - 2; i++) {
				top3LotteryNum += lotteryNums[i];
			}
			List<String> selectedNoWithSplit = new ArrayList<>();
			int selectedNoCount = selectedNo.length();
			for (int c = 0; c < selectedNoCount - 1; c++) {
				for (int p = c + 1; p < selectedNoCount; p++) {
					StringBuilder sb = new StringBuilder();
					sb.append(selectedNo.charAt(c));
					sb.append(selectedNo.charAt(p));
					selectedNoWithSplit.add(sb.toString());
				}
			}
			for (String num : selectedNoWithSplit) {
				if (top3LotteryNum.contains(String.valueOf(num.charAt(0)))
						&& top3LotteryNum.contains(String.valueOf(num.charAt(1)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_中三二码("CQSSC_Z32M", "中三二码") {

		/**
		 * 从0-9中至少选择2个号码投注，竞猜开奖号码中三位中包含这2个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：*27**投注号码：7,2
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String middle3LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 1; i < lotteryNums.length - 1; i++) {
				middle3LotteryNum += lotteryNums[i];
			}
			List<String> selectedNoWithSplit = new ArrayList<>();
			int selectedNoCount = selectedNo.length();
			for (int c = 0; c < selectedNoCount - 1; c++) {
				for (int p = c + 1; p < selectedNoCount; p++) {
					StringBuilder sb = new StringBuilder();
					sb.append(selectedNo.charAt(c));
					sb.append(selectedNo.charAt(p));
					selectedNoWithSplit.add(sb.toString());
				}
			}
			for (String num : selectedNoWithSplit) {
				if (middle3LotteryNum.contains(String.valueOf(num.charAt(0)))
						&& middle3LotteryNum.contains(String.valueOf(num.charAt(1)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_后三二码("CQSSC_H32M", "后三二码") {

		/**
		 * 从0-9中至少选择2个号码作为一注，开奖号码后三位中包含这2个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：**32x 投注号码：3,2
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String end3LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 2; i < lotteryNums.length; i++) {
				end3LotteryNum += lotteryNums[i];
			}
			List<String> selectedNoWithSplit = new ArrayList<>();
			int selectedNoCount = selectedNo.length();
			for (int c = 0; c < selectedNoCount - 1; c++) {
				for (int p = c + 1; p < selectedNoCount; p++) {
					StringBuilder sb = new StringBuilder();
					sb.append(selectedNo.charAt(c));
					sb.append(selectedNo.charAt(p));
					selectedNoWithSplit.add(sb.toString());
				}
			}
			for (String num : selectedNoWithSplit) {
				if (end3LotteryNum.contains(String.valueOf(num.charAt(0)))
						&& end3LotteryNum.contains(String.valueOf(num.charAt(1)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_四星一码("CQSSC_4X1M", "四星一码") {

		/**
		 * 从0-9中至少选择1个号码投注，竞猜开奖号码后四位中包含这个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：**7**投注号码：7
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String end4LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 1; i < lotteryNums.length; i++) {
				end4LotteryNum += lotteryNums[i];
			}
			for (int i = 0; i < selectedNo.length(); i++) {
				if (end4LotteryNum.contains(String.valueOf(selectedNo.charAt(i)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_四星二码("CQSSC_4X2M", "四星二码") {

		/**
		 * 从0-9中至少选择2个号码投注，竞猜开奖号码后四位中包含这2个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：*7*2*投注号码：7,2
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			String end4LotteryNum = "";
			String[] lotteryNums = lotteryNum.split(",");
			for (int i = 1; i < lotteryNums.length; i++) {
				end4LotteryNum += lotteryNums[i];
			}
			List<String> selectedNoWithSplit = new ArrayList<>();
			int selectedNoCount = selectedNo.length();
			for (int c = 0; c < selectedNoCount - 1; c++) {
				for (int p = c + 1; p < selectedNoCount; p++) {
					StringBuilder sb = new StringBuilder();
					sb.append(selectedNo.charAt(c));
					sb.append(selectedNo.charAt(p));
					selectedNoWithSplit.add(sb.toString());
				}
			}
			for (String num : selectedNoWithSplit) {
				if (end4LotteryNum.contains(String.valueOf(num.charAt(0)))
						&& end4LotteryNum.contains(String.valueOf(num.charAt(1)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_五星二码("CQSSC_5X2M", "五星二码") {

		/**
		 * 从0-9中至少选择2个号码投注，竞猜开奖号码中包含这2个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：*7*2*投注号码：7,2
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			List<String> selectedNoWithSplit = new ArrayList<>();
			int selectedNoCount = selectedNo.length();
			for (int c = 0; c < selectedNoCount - 1; c++) {
				for (int p = c + 1; p < selectedNoCount; p++) {
					StringBuilder sb = new StringBuilder();
					sb.append(selectedNo.charAt(c));
					sb.append(selectedNo.charAt(p));
					selectedNoWithSplit.add(sb.toString());
				}
			}
			for (String num : selectedNoWithSplit) {
				if (lotteryNum.contains(String.valueOf(num.charAt(0)))
						&& lotteryNum.contains(String.valueOf(num.charAt(1)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_五星三码("CQSSC_5X3M", "五星三码") {

		/**
		 * 从0-9中至少选择3个号码投注，竞猜开奖号码中包含这3个号码，包含即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：*7*21投注号码：7,2,1
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int winningCount = 0;
			List<String> selectedNoWithSplit = new ArrayList<>();
			int selectedNoCount = selectedNo.length();
			for (int c = 0; c < selectedNoCount - 2; c++) {
				for (int p = c + 1; p < selectedNoCount - 1; p++) {
					for (int q = p + 1; q < selectedNoCount; q++) {
						StringBuilder sb = new StringBuilder();
						sb.append(selectedNo.charAt(c));
						sb.append(selectedNo.charAt(p));
						sb.append(selectedNo.charAt(q));
						selectedNoWithSplit.add(sb.toString());
					}
				}
			}
			for (String num : selectedNoWithSplit) {
				if (lotteryNum.contains(String.valueOf(num.charAt(0)))
						&& lotteryNum.contains(String.valueOf(num.charAt(1)))
						&& lotteryNum.contains(String.valueOf(num.charAt(2)))) {
					winningCount++;
				}
			}
			return winningCount;
		}
	},

	重庆时时彩_任二直选单式("CQSSC_R2ZXDS", "重庆时时彩_任二直选单式") {

		/**
		 * 在万位、千位、百位、十位、个位上的任意两个位置，各选1个号码组成一注，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：2*3** 投注号码：万位：2 百位：3
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int count = 0;
			String[] lotteryNums = lotteryNum.split(",");
			String[] selectedNos = selectedNo.split(",");
			for (int i = 0; i < lotteryNums.length; i++) {
				if (lotteryNums[i].equals(selectedNos[i])) {
					count++;
					if (count == 2) {
						return 1;
					}
				}
			}
			return 0;
		}

	},

	重庆时时彩_任二直选("CQSSC_R2ZX", "重庆时时彩_任二直选") {

		/**
		 * 在万位、千位、百位、十位、个位上的任意两个位置，各选1个号码组成一注，号码和顺序都一致即中奖。<br/>
		 * 中奖实例:<br/>
		 * 开奖号码：2*3** 投注号码：万位：2 百位：3
		 */
		@Override
		public int calcWinningCount(String lotteryNum, String selectedNo) {
			int[][] selectedBalls = BallUtils.buildSSCBall(selectedNo);
			List<String> selectedNoWithSplit = new ArrayList<>();
			for (int c = 0; c < 4; c++) {
				for (int p = c + 1; p < 5; p++) {
					for (int q = 0; q < selectedBalls[c].length; q++) {
						for (int r = 0; r < selectedBalls[p].length; r++) {
							List<String> list = new ArrayList<>();
							for (int s = 0; s < c; s++) {
								list.add("-");
							}
							list.add(selectedBalls[c][q] + "");
							for (int s = 0; s < p - c - 1; s++) {
								list.add("-");
							}
							list.add(selectedBalls[p][r] + "");
							for (int s = 0; s < 5 - p - 1; s++) {
								list.add("-");
							}
							selectedNoWithSplit.add(String.join(",", list));
						}
					}
				}
			}
			int winningCount = 0;
			for (String num : selectedNoWithSplit) {
				winningCount += 重庆时时彩_任二直选单式.calcWinningCount(lotteryNum, num);
			}
			return winningCount;
		}

	};

	private String code;

	private String name;

	/**
	 * 计算中奖次数
	 * 
	 * @param lotteryNum
	 * @param selectedNo
	 * @return
	 */
	public abstract int calcWinningCount(String lotteryNum, String selectedNo);

	/**
	 * 根据code获取对应的玩法
	 * 
	 * @param code
	 * @return
	 */
	public static GamePlayEnum getPlay(String code) {
		for (GamePlayEnum play : GamePlayEnum.values()) {
			if (play.getCode().equals(code)) {
				return play;
			}
		}
		return null;
	}

}
