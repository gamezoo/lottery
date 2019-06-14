package me.zohar.lottery.issue.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
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
public class YnsscService {

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
		issueService.syncLotteryNum(Constant.游戏_云南时时彩, latestWithInterface.getIssueNum(),
				latestWithInterface.getLotteryNum());
	}

	public IssueVO getLatestLotteryResultWithApi() {
		List<IssueVO> issues = new ArrayList<>();
		CountDownLatch countlatch = new CountDownLatch(3);
		List<Future<IssueVO>> futures = new ArrayList<>();

		futures.add(ThreadPoolUtils.getSyncLotteryThreadPool().submit(() -> {
			return getLatestLotteryResultWithBowangcai();
		}));
		futures.add(ThreadPoolUtils.getSyncLotteryThreadPool().submit(() -> {
			return getLatestLotteryResultWithSsqzj();
		}));
		futures.add(ThreadPoolUtils.getSyncLotteryThreadPool().submit(() -> {
			return getLatestLotteryResultWithCjcp();
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

	public IssueVO getLatestLotteryResultWithBowangcai() {
		String currentDateFormat = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("typeid", "37");
			paramMap.put("dates", currentDateFormat);
			String url = "http://www.bowangcai.com/lottery/s/tid/37/times/{0}";
			url = MessageFormat.format(url, currentDateFormat);
			String result = HttpUtil.post(url, paramMap);
			JSONObject resultJsonObject = JSON.parseObject(result);
			JSONObject jsonObject = resultJsonObject.getJSONObject("rsm").getJSONObject("info").getJSONArray("list")
					.getJSONObject(0);

			String lotteryNum = jsonObject.getString("lottery_numbers");
			long issueNum = jsonObject.getLong("lottery_issue");
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过bowangcai.com获取云南时时彩最新开奖结果发生异常", e);
		}
		return null;
	}

	public IssueVO getLatestLotteryResultWithCjcp() {
		try {
			String url = "https://shishicai.cjcp.com.cn/yunnan/kaijiang/";
			Document document = Jsoup.connect(url).get();
			Element tr = document.getElementsByClass("kjjg_table").get(0).getElementsByTag("tr").get(1);
			Elements tds = tr.getElementsByTag("td");

			List<String> lotteryNums = new ArrayList<>();
			Elements lotteryNumElements = tds.get(2).getElementsByClass("hm_bg");
			for (Element lotteryNumElement : lotteryNumElements) {
				lotteryNums.add(lotteryNumElement.text());
			}
			String lotteryNum = String.join(",", lotteryNums);
			long issueNum = Long.parseLong(tds.get(0).text().substring(0, 11));
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过shishicai.cjcp.com.cn获取云南时时彩最新开奖结果发生异常", e);
		}
		return null;
	}

	public IssueVO getLatestLotteryResultWithSsqzj() {
		try {
			String url = "https://www.ssqzj.com/ssc/ynssc/";
			Document document = Jsoup.connect(url).get();

			List<String> lotteryNums = new ArrayList<>();
			Elements lotteryNumElements = document.getElementById("kjnum").getElementsByTag("em");
			for (Element lotteryNumElement : lotteryNumElements) {
				lotteryNums.add(lotteryNumElement.text());
			}
			String lotteryNum = String.join(",", lotteryNums);
			long issueNum = Long.parseLong(document.getElementById("kjqs").text());
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过ssqzj.com获取云南时时彩最新开奖结果发生异常", e);
		}
		return null;
	}

}
