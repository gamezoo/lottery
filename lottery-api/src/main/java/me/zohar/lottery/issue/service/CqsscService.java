package me.zohar.lottery.issue.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.dom4j.DocumentHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.common.utils.ThreadPoolUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.vo.IssueVO;

@Service
@Slf4j
public class CqsscService {

	@Autowired
	private IssueService issueService;

	/**
	 * 同步当前时间的开奖号码
	 */
	public void syncLotteryNum() {
		IssueVO latestWithInterface = getLatestLotteryResultWithApi();
		if (latestWithInterface == null) {
			return;
		}
		issueService.syncLotteryNum(Constant.游戏_重庆时时彩, latestWithInterface.getIssueNum(),
				latestWithInterface.getLotteryNum());
	}

	public IssueVO getLatestLotteryResultWithApi() {
		List<IssueVO> issues = new ArrayList<>();
		CountDownLatch countlatch = new CountDownLatch(3);
		List<Future<IssueVO>> futures = new ArrayList<>();
		futures.add(ThreadPoolUtils.getSyncLotteryThreadPool().submit(() -> {
			return getLatestLotteryIssueWithAiCai();
		}));
		futures.add(ThreadPoolUtils.getSyncLotteryThreadPool().submit(() -> {
			return getLatestLotteryResultWith500();
		}));
		futures.add(ThreadPoolUtils.getSyncLotteryThreadPool().submit(() -> {
			return getLatestLotteryResultWithOpenCai();
		}));
		for (Future<IssueVO> future : futures) {
			try {
				IssueVO issueVO = future.get(3, TimeUnit.SECONDS);
				issues.add(issueVO);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				log.error("异步future接口出现错误", e);
			}
			countlatch.countDown();
		}
		try {
			countlatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		issues.sort(new Comparator<IssueVO>() {

			@Override
			public int compare(IssueVO o1, IssueVO o2) {
				if (o1 == null) {
					return -1;
				}
				if (o2 == null) {
					return -1;
				}
				if (o1 != null && o2 != null) {
					return o2.getIssueNum().compareTo(o1.getIssueNum());
				}
				return 0;
			}
		});
		return issues.isEmpty() ? null : issues.get(0);
	}

	/**
	 * 通过新浪爱彩网获取重庆时时彩最新开奖期号
	 * 
	 * @return
	 */
	public IssueVO getLatestLotteryIssueWithAiCai() {
		try {
			String url = "https://kaijiang.aicai.com/cqssc/";
			Document document = Jsoup.connect(url).get();
			Element element = document.getElementById("jq_body_kc_result").child(0);
			String issueText = element.child(0).text();
			long issueNum = Long.parseLong(issueText.substring(0, issueText.length() - 1).replace("-", ""));
			String lotteryNum = element.child(2).text().replace("|", ",");
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过新浪爱彩网获取重庆时时彩最新开奖结果发生异常", e);
		}
		return null;
	}

	/**
	 * 通过500彩票网获取重庆时时彩最新开奖结果
	 * 
	 * @return
	 */
	public IssueVO getLatestLotteryResultWith500() {
		String currentDateFormat = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
		try {
			String url = "https://kaijiang.500.com/static/public/ssc/xml/qihaoxml/{0}.xml";
			url = MessageFormat.format(url, currentDateFormat);
			String result = HttpUtil.get(url);
			org.dom4j.Document document = DocumentHelper.parseText(result);
			List<org.dom4j.Element> elements = document.getRootElement().elements("row");
			org.dom4j.Element element = elements.get(0);
			long issueNum = Long.parseLong(element.attributeValue("expect"));
			String lotteryNum = element.attributeValue("opencode");
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过500彩票网获取重庆时时彩最新开奖结果发生异常", e);
		}
		return null;
	}

	/**
	 * 通过开彩票接口获取重庆时时彩最新开奖结果
	 */
	public IssueVO getLatestLotteryResultWithOpenCai() {
		try {
			String result = HttpUtil.get("http://f.apiplus.net/cqssc.json");
			log.info("接口返回结果:{}", result);
			JSONObject resultJsonObject = JSON.parseObject(result);
			JSONArray jsonArray = resultJsonObject.getJSONArray("data");
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			long issueNum = Long.parseLong(jsonObject.getString("expect"));
			String lotteryNum = jsonObject.getString("opencode");
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过开彩票获取重庆时时彩最新开奖结果发生异常", e);
		}
		return null;
	}

}
