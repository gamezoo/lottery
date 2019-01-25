package me.zohar.lottery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.issue.vo.IssueVO;

public class XjsscLotteryIssueTest {

	@Test
	public void test() {
		getLatestLotteryIssueWithXjflcp();
	}

	/**
	 * 通过新疆福利彩票官网获取新疆时时彩最新开奖期号
	 * 
	 * @return
	 */
	public IssueVO getLatestLotteryIssueWithXjflcp() {
		try {
			String url = "http://www.xjflcp.com/game/sscIndex";
			Document document = Jsoup.connect(url).get();
			Element element = document.getElementsByClass("con_left").get(1);
			long issueNum = Long.parseLong(element.getElementsByTag("span").get(0).text());
			String lotteryNum = element.getElementsByTag("i").text().replace(" ", ",");
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null)
					.lotteryNum(lotteryNum).build();
			return lotteryResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
