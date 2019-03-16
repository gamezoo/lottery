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
import net.bytebuddy.build.Plugin.Engine.PoolStrategy.Eager;

public class SpelTest {

	@Test
	public void test() {
		IssueVO lotteryResult = getLatestLotteryResultWith06kj77();
		System.out.println(lotteryResult.getIssueNum());
		System.out.println(lotteryResult.getLotteryNum());
	}

	public IssueVO getLatestLotteryResultWith06kj77() {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("lType", "26");
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
			e.printStackTrace();
		}
		return null;
	}

}
