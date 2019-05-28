import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import groovy.lang.GroovyShell;
import me.zohar.lottery.information.vo.LotteryInformationVO;

List<LotteryInformationVO> vos = new ArrayList<>();
		String url = "http://www.zhcw.com/xinwen/caizhongxinwen/";
		Elements elements = Jsoup.connect(url).get().getElementsByClass("Nlistul").first().getElementsByTag("li");
		for (Element element : elements) {
			Element timeTag = element.getElementsByClass("Ntime").first();
			if (timeTag == null) {
				continue;
			}
			// String dateFormat = timeTag.text();
			Element urlTag = element.getElementsByTag("a").first();
			String newUrl = "http://www.zhcw.com" + urlTag.attr("href");
			Document document = Jsoup.connect(newUrl).get();
			String title = document.getElementsByClass("newsTitle").first().text();
			String content = document.getElementById("news_content").html();
			LotteryInformationVO vo = new LotteryInformationVO();
			vo.setTitle(title);
			vo.setContent(content);
			vos.add(vo);
		}
		return vos;