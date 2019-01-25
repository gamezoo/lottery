package me.zohar.lottery.gd11x5.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.gd11x5.domain.Gd11X5LotteryResult;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Gd11X5LotteryResultVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 期号
	 */
	private Long issue;

	/**
	 * 开奖日期:yyyy-MM-dd
	 */
	private String lotteryDate;

	/**
	 * 全部开奖号码,以逗号分隔
	 */
	private String lotteryNum;

	/**
	 * 开奖号码第1位
	 */
	private Integer lotteryNum1;

	/**
	 * 开奖号码第2位
	 */
	private Integer lotteryNum2;

	/**
	 * 开奖号码第3位
	 */
	private Integer lotteryNum3;

	/**
	 * 开奖号码第4位
	 */
	private Integer lotteryNum4;

	/**
	 * 开奖号码第5位
	 */
	private Integer lotteryNum5;

	public static Gd11X5LotteryResultVO with(Long issue, String lotteryNum) {
		Gd11X5LotteryResultVO vo = new Gd11X5LotteryResultVO();
		vo.setIssue(issue);
		vo.setLotteryNum(lotteryNum);
		return vo;
	}

	public static List<Gd11X5LotteryResultVO> convertFor(List<Gd11X5LotteryResult> gd11X5LotteryResults) {
		if (CollectionUtil.isEmpty(gd11X5LotteryResults)) {
			return new ArrayList<>();
		}
		List<Gd11X5LotteryResultVO> vos = new ArrayList<>();
		for (Gd11X5LotteryResult gd11x5LotteryResult : gd11X5LotteryResults) {
			vos.add(convertFor(gd11x5LotteryResult));
		}
		return vos;
	}

	public static Gd11X5LotteryResultVO convertFor(Gd11X5LotteryResult gd11X5LotteryResult) {
		if (gd11X5LotteryResult == null) {
			return null;
		}
		Gd11X5LotteryResultVO vo = new Gd11X5LotteryResultVO();
		BeanUtils.copyProperties(gd11X5LotteryResult, vo);
		vo.setLotteryDate(DateUtil.format(gd11X5LotteryResult.getLotteryDate(), DatePattern.NORM_DATE_PATTERN));
		return vo;
	}

	public Gd11X5LotteryResult convertToPo() {
		Gd11X5LotteryResult po = new Gd11X5LotteryResult();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setLotteryDate(DateUtil.beginOfDay(DateUtil.parse(this.getLotteryDate(), DatePattern.NORM_DATE_PATTERN)));
		po.setSyncTime(new Date());
		return po;
	}

}
