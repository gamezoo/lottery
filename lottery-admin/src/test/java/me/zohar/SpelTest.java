package me.zohar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import me.zohar.lottery.information.vo.LotteryInformationVO;

public class SpelTest {

	@Test
	public void test() throws IOException {
		testInner();
	}

	public List<LotteryInformationVO> testInner() throws IOException {
		List<LotteryInformationVO> vos = new ArrayList<>();
		String url = "http://www.zhcw.com/xinwen/caizhongxinwen/";
		Elements elements = Jsoup.connect(url).get().getElementsByClass("Nlistul").first().getElementsByTag("li");
		for (Element element : elements) {
			Element timeTag = element.getElementsByClass("Ntime").first();
			if (timeTag == null) {
				continue;
			}
			Element urlTag = element.getElementsByTag("a").first();
			String newUrl = "http://www.zhcw.com" + urlTag.attr("href");
			Document document = Jsoup.connect(newUrl).get();
			if (document == null) {
				continue;
			}
			Element contentElement = document.getElementById("news_content");
			Elements imgs = contentElement.getElementsByTag("img");
			for (Element img : imgs) {
				String imgSrc = "http://www.zhcw.com" + img.attr("src");
				img.attr("src", imgSrc);
			}
			String title = document.getElementsByClass("newsTitle").first().text();
			String content = document.getElementById("news_content").html();
			String[] split = document.getElementsByClass("message").first().text().split(" ");
			String publishTime = split[0] + " " + split[1];
			String source = split[2];
			source = source.substring(3, source.length());
			LotteryInformationVO vo = new LotteryInformationVO();
			vo.setTitle(title);
			vo.setContent(content);
			vo.setPublishTime(DateUtil.parse(publishTime, DatePattern.NORM_DATETIME_PATTERN));
			vo.setSource(source);
			vos.add(vo);
		}
		return vos;
	}

}
