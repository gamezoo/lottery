package me.zohar.lottery.issue.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.constants.GameCode;
import me.zohar.lottery.issue.vo.IssueVO;

@Service
@Slf4j
public class XjsscService {

	@Autowired
	private IssueService issueService;

	/**
	 * 同步当前时间的开奖号码
	 */
	public void syncLotteryNum() {
		IssueVO latestWithInterface = getLatestLotteryIssueWithXjflcp();
		if (latestWithInterface == null) {
			return;
		}
		issueService.syncLotteryNum(GameCode.新疆时时彩, latestWithInterface.getIssueNum(),
				latestWithInterface.getLotteryNum());
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
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过新疆福利彩票官网获取新疆时时彩最新开奖期号发生异常", e);
		}
		return null;
	}

}
