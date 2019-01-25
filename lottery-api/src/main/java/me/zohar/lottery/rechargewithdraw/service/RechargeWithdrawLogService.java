package me.zohar.lottery.rechargewithdraw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.rechargewithdraw.domain.RechargeWithdrawLog;
import me.zohar.lottery.rechargewithdraw.param.RechargeWithdrawLogQueryCondParam;
import me.zohar.lottery.rechargewithdraw.repo.RechargeWithdrawLogRepo;
import me.zohar.lottery.rechargewithdraw.vo.RechargeWithdrawLogVO;

@Service
public class RechargeWithdrawLogService {

	@Autowired
	private RechargeWithdrawLogRepo rechargeWithdrawLogRepo;

	@Transactional(readOnly = true)
	public PageResult<RechargeWithdrawLogVO> findMyRechargeWithdrawLogByPage(RechargeWithdrawLogQueryCondParam param) {
		return findRechargeWithdrawLogByPage(param);
	}

	@Transactional(readOnly = true)
	public PageResult<RechargeWithdrawLogVO> findRechargeWithdrawLogByPage(RechargeWithdrawLogQueryCondParam param) {
		Specification<RechargeWithdrawLog> spec = new Specification<RechargeWithdrawLog>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<RechargeWithdrawLog> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getOrderType())) {
					predicates.add(builder.equal(root.get("orderType"), param.getOrderType()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				if (StrUtil.isNotEmpty(param.getUserAccountId())) {
					predicates.add(builder.equal(root.get("userAccountId"), param.getUserAccountId()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<RechargeWithdrawLog> result = rechargeWithdrawLogRepo.findAll(spec, PageRequest.of(param.getPageNum() - 1,
				param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<RechargeWithdrawLogVO> pageResult = new PageResult<>(
				RechargeWithdrawLogVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

}
