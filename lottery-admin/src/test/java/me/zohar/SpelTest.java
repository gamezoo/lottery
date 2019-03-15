package me.zohar;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import me.zohar.lottery.issue.vo.IssueVO;

public class SpelTest {

	@Test
	public void test() {
		IssueVO lotteryResult = getLatestLotteryResultWith06kj77();
		System.out.println(lotteryResult.getIssueNum());
		System.out.println(lotteryResult.getLotteryNum());
	}

	public IssueVO getLatestLotteryResultWith06kj77() {
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
			e.printStackTrace();
		}
		return null;
	}

}
