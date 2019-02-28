package me.zohar.lottery.issue.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		IssueVO latestWithInterface = getLatestLotteryResultWithKjh();
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

	public IssueVO getLatestLotteryResultWithKjh() {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("lotterytype", "GP_SSC_XinJiang");
			String result = HttpUtil.post("https://kjh.55128.cn/k/Template/_getKjData", paramMap);
			JSONObject resultJsonObject = JSON.parseObject(result);
			long issueNum = Long.parseLong("20" + resultJsonObject.getString("Term"));
			String lotteryDateFormat = DateUtil.format(
					DateUtil.parse(String.valueOf(issueNum).substring(0, 8), DatePattern.PURE_DATE_PATTERN),
					DatePattern.NORM_DATE_PATTERN);
			List<String> lotteryNums = new ArrayList<>();
			JSONArray jsonArray = resultJsonObject.getJSONArray("RedBall");
			for (int i = 0; i < jsonArray.size(); i++) {
				lotteryNums.add(jsonArray.getString(i));
			}
			String lotteryNum = String.join(",", lotteryNums);
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(lotteryDateFormat)
					.lotteryNum(lotteryNum).build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过开奖助手获取新疆时时彩最新开奖结果发生异常", e);
		}
		return null;
	}

}
