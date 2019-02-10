package me.zohar.lottery.useraccount.test;

import java.io.IOException;
import java.text.MessageFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class ResultTest {

	@Test
	public void test() throws IOException  {
		String url = "https://kaijiang.aicai.com/cqssc/";
		Document document = Jsoup.connect(url).get();
		Element element = document.getElementById("jq_body_kc_result").child(0);
		String issueText = element.child(0).text();
		System.out.println(issueText.substring(0, issueText.length() - 1).replace("-", ""));
		System.out.println(element.child(2).text().replace("|", ","));
	}

}
