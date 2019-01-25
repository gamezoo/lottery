package me.zohar.lottery.issue.service;

import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.GameCode;
import me.zohar.lottery.issue.domain.Issue;
import me.zohar.lottery.issue.repo.IssueRepo;
import me.zohar.lottery.issue.vo.IssueVO;

@Service
@Slf4j
public class XjsscService {

	@Autowired
	private IssueService issueService;

	@Autowired
	private IssueRepo issuetRepo;

	public void generateIssue(Date currentDate) {
		for (int i = 0; i < 5; i++) {
			Date lotteryDate = DateUtil.offset(DateUtil.beginOfDay(currentDate), DateField.DAY_OF_MONTH, i);
			List<Issue> issues = issuetRepo.findByGameCodeAndLotteryDateOrderByLotteryTimeDesc(GameCode.新疆时时彩,
					lotteryDate);
			if (CollectionUtil.isNotEmpty(issues)) {
				continue;
			}
			generateLotteryResultInner(lotteryDate);
		}
	}

	public void generateLotteryResultInner(Date lotteryDate) {
		String lotteryDateFormat = DateUtil.format(lotteryDate, DatePattern.PURE_DATE_PATTERN);
		// 生成10点到凌晨2点的开奖结果数据,10分钟一期,共96期
		for (int i = 0; i < 96; i++) {
			long issueNum = Long.parseLong(lotteryDateFormat + String.format("%02d", i + 1));
			Date startTime = DateUtil.offset(lotteryDate, DateField.MINUTE, 60 * 10 + i * 10);
			Date endTime = DateUtil.offset(startTime, DateField.MINUTE, 10);
			Issue issue = Issue.builder().id(IdUtils.getId()).gameCode(GameCode.新疆时时彩).lotteryDate(lotteryDate)
					.lotteryTime(endTime).issueNum(issueNum).startTime(startTime).endTime(endTime).build();
			issuetRepo.save(issue);
		}
	}

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
