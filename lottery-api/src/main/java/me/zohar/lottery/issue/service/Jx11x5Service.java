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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.common.utils.ThreadPoolUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.vo.IssueVO;

@Service
@Slf4j
public class Jx11x5Service {

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
		issueService.syncLotteryNum(Constant.游戏_江西11选5, latestWithInterface.getIssueNum(),
				latestWithInterface.getLotteryNum());
	}

	public IssueVO getLatestLotteryResultWithApi() {
		List<IssueVO> issues = new ArrayList<>();
		CountDownLatch countlatch = new CountDownLatch(1);
		List<Future<IssueVO>> futures = new ArrayList<>();
		futures.add(ThreadPoolUtils.getSyncLotteryThreadPool().submit(() -> {
			return getLatestLotteryResultWithKjh();
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

	public IssueVO getLatestLotteryResultWithKjh() {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("lotterytype", "GP_11x5_JiangXi");
			String result = HttpUtil.post("https://kjh.55128.cn/k/Template/_getKjData", paramMap);
			JSONObject resultJsonObject = JSON.parseObject(result);
			long issueNum = Long.parseLong("20" + resultJsonObject.getString("Term"));
			List<String> lotteryNums = new ArrayList<>();
			JSONArray jsonArray = resultJsonObject.getJSONArray("RedBall");
			for (int i = 0; i < jsonArray.size(); i++) {
				lotteryNums.add(String.format("%02d", Integer.parseInt(jsonArray.getString(i))));
			}
			String lotteryNum = String.join(",", lotteryNums);
			IssueVO lotteryResult = IssueVO.builder().issueNum(issueNum).lotteryDate(null).lotteryNum(lotteryNum)
					.build();
			return lotteryResult;
		} catch (Exception e) {
			log.error("通过开奖助手获取江西11选5最新开奖结果发生异常", e);
		}
		return null;
	}

}
