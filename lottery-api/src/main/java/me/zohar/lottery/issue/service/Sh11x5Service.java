package me.zohar.lottery.issue.service;

import java.util.ArrayList;
import java.util.Comparator;
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

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.common.utils.ThreadPoolUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.vo.IssueVO;

@Service
@Slf4j
public class Sh11x5Service {

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
		issueService.syncLotteryNum(Constant.游戏_上海11选5, latestWithInterface.getIssueNum(),
				latestWithInterface.getLotteryNum());
	}

	public IssueVO getLatestLotteryResultWithApi() {
		List<IssueVO> issues = new ArrayList<>();
		CountDownLatch countlatch = new CountDownLatch(1);
		List<Future<IssueVO>> futures = new ArrayList<>();
		futures.add(ThreadPoolUtils.getSyncLotteryThreadPool().submit(() -> {
			return getLatestLotteryResultWithC8();
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

	public IssueVO getLatestLotteryResultWithC8() {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("lType", "31");
			String result = HttpUtil.post("https://c8.cn/News/GetLastBetInfo", paramMap);
			JSONObject resultJsonObject = JSON.parseObject(result);

			long issueNum = Long.parseLong(resultJsonObject.getString("Issue"));

			List<String> lotteryNums = new ArrayList<>();
			Document document = Jsoup.parse(resultJsonObject.getString("NumHtml"));
			Elements lotteryNumElements = document.getElementsByTag("span");
			for (Element lotteryNumElement : lotteryNumElements) {
				lotteryNums.add(lotteryNumElement.text());
			}
			String lotteryNum = String.join(",", lotteryNums);
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过c8.cn获取上海11选5最新开奖结果发生异常", e);
		}
		return null;
	}

}
