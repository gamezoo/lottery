package me.zohar.lottery.gd11x5.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.gd11x5.domain.Gd11X5LotteryResult;
import me.zohar.lottery.gd11x5.repo.Gd11X5LotteryResultRepo;
import me.zohar.lottery.gd11x5.vo.Gd11X5LotteryResultVO;

@Service
@Slf4j
public class Gd11X5LotteryResultService {

	@Autowired
	private Gd11X5LotteryResultRepo lotteryResultRepo;

	/**
	 * 获取最近5次的开奖结果
	 * 
	 * @return
	 */
	public List<Gd11X5LotteryResultVO> findLatelyThe5TimeLotteryResult() {
		List<Gd11X5LotteryResult> lastLotteryResults = lotteryResultRepo.findTop5ByOrderByIssueDesc();
		return Gd11X5LotteryResultVO.convertFor(lastLotteryResults);
	}

	/**
	 * 获取当前日期最新的开奖结果
	 * 
	 * @return
	 */
	public Gd11X5LotteryResultVO getLastLotteryResult(Date currentDate) {
		Gd11X5LotteryResult lastLotteryResult = lotteryResultRepo
				.findTopByLotteryDateOrderByIssueDesc(DateUtil.beginOfDay(currentDate));
		return Gd11X5LotteryResultVO.convertFor(lastLotteryResult);
	}

	/**
	 * 同步当前日期的开奖结果
	 * 
	 * @param currentDate
	 */
	public void syncLotteryResult(Date currentDate) {
		Gd11X5LotteryResultVO lastLotteryResult = getLastLotteryResult(currentDate);
		List<Gd11X5LotteryResultVO> allLotteryResult = getCurrentDateAllLotteryResultWithGdLottery(currentDate);
		if (CollectionUtil.isEmpty(allLotteryResult)) {
			return;
		}

		Gd11X5LotteryResultVO lastWithInterface = allLotteryResult.get(allLotteryResult.size() - 1);
		// 若最新的开奖结果为空(即当天还没开始)或当前保存到数据库的最新开奖结果不是最新期号,则将最新期号的开奖结果入库
		if (lastLotteryResult == null
				|| (lastLotteryResult != null && lastLotteryResult.getIssue() < lastWithInterface.getIssue())) {
			lotteryResultRepo.save(lastWithInterface.convertToPo());
		}
	}

	/**
	 * 通过广东体彩网获取当前日期所有的开奖结果
	 */
	List<Gd11X5LotteryResultVO> getCurrentDateAllLotteryResultWithGdLottery(Date currentDate) {
		String currentDateFormat = DateUtil.format(currentDate, DatePattern.NORM_DATE_PATTERN);
		try {
			List<Gd11X5LotteryResultVO> lotteryResults = new ArrayList<>();
			String url = "https://www.gdlottery.cn/odata/zst11xuan5.jspx?method=to11x5kjggzst&date={0}";
			url = MessageFormat.format(url, currentDateFormat);
			Document document = Jsoup.connect(url).get();
			Element parent = document.getElementById("reloadId").parent();
			int childNodeSize = parent.childNodeSize();
			for (int i = 2; i < (childNodeSize / 2) - 2; i++) {
				Element element = parent.child(i);
				if (element.childNodeSize() == 0) {
					continue;
				}
				// 期号格式:18092384
				long issue = Long.parseLong(element.child(0).text());
				String lotteryDateFormat = DateUtil.format(
						DateUtil.parse("20" + String.valueOf(issue).substring(0, 6), DatePattern.PURE_DATE_PATTERN),
						DatePattern.NORM_DATE_PATTERN);
				// 若返回的开奖结果的开奖日期跟传入的日期不同,则剔除该记录
				if (!currentDateFormat.equals(lotteryDateFormat)) {
					continue;
				}

				String lotteryNum = StrUtil.trim(element.child(1).text()).replace("，", ",");
				String[] split = lotteryNum.split(",");
				Gd11X5LotteryResultVO lotteryResult = Gd11X5LotteryResultVO.builder().issue(issue)
						.lotteryDate(lotteryDateFormat).lotteryNum(lotteryNum).lotteryNum1(Integer.parseInt(split[0]))
						.lotteryNum2(Integer.parseInt(split[1])).lotteryNum3(Integer.parseInt(split[2]))
						.lotteryNum4(Integer.parseInt(split[3])).lotteryNum5(Integer.parseInt(split[4])).build();
				lotteryResults.add(lotteryResult);
			}
			return lotteryResults;
		} catch (Exception e) {
			String msg = MessageFormat.format("获取广东11选5开奖结果发生异常,开奖日期为:{0}", currentDateFormat);
			log.error(msg, e);
		}
		return null;
	}

}
