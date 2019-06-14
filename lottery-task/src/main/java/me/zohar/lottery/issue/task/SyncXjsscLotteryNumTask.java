package me.zohar.lottery.issue.task;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xxl.mq.client.consumer.IMqConsumer;
import com.xxl.mq.client.consumer.MqResult;
import com.xxl.mq.client.consumer.annotation.MqConsumer;
import com.xxl.mq.client.message.XxlMqMessage;
import com.xxl.mq.client.producer.XxlMqProducer;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.issue.param.SyncLotteryNumMsg;
import me.zohar.lottery.issue.service.IssueService;
import me.zohar.lottery.issue.service.XjsscService;
import me.zohar.lottery.issue.vo.IssueVO;

@MqConsumer(topic = SyncXjsscLotteryNumTask.TOPIC)
@Component
@Slf4j
public class SyncXjsscLotteryNumTask implements IMqConsumer {

	public static final String TOPIC = "SYNC_LOTTERY_NUM_XJSSC";

	private static final List<Integer> RETRY_LEVEL = Arrays.asList(65, 25, 18, 18, 16, 16, 14, 14, 10, 10, 25, 25, 18,
			18);

	@Autowired
	private XjsscService xjsscService;

	@Autowired
	private IssueService issueService;

	@Override
	public MqResult consume(String data) throws Exception {
		SyncLotteryNumMsg msg = JSON.parseObject(data, SyncLotteryNumMsg.class);
		IssueVO latelyIssue = issueService.getLatelyIssue(msg.getGameCode());
		if (msg.getIssueNum().compareTo(latelyIssue.getIssueNum()) != 0) {
			return MqResult.SUCCESS;
		}
		
		Boolean syncSuccessFlag = false;
		try {
			log.info("执行同步新疆时时彩开奖号码定时任务start");
			xjsscService.syncLotteryNum();
			IssueVO issue = issueService.findByGameCodeAndIssueNum(msg.getGameCode(), msg.getIssueNum());
			syncSuccessFlag = !Constant.期号状态_未开奖.equals(issue.getState());
			log.info("执行同步新疆时时彩开奖号码定时任务end");
		} catch (Exception e) {
			log.error("执行同步新疆时时彩开奖号码定时任务发生异常", e);
		}
		if (!syncSuccessFlag) {
			if (msg.getRetries() < RETRY_LEVEL.size() - 1) {
				Date effectTime = DateUtil.offset(new Date(), DateField.SECOND, RETRY_LEVEL.get(msg.getRetries()));
				msg.setRetries(msg.getRetries() + 1);
				XxlMqProducer.produce(new XxlMqMessage(TOPIC, JSON.toJSONString(msg), effectTime));
			}
		}
		return MqResult.SUCCESS;
	}

}
