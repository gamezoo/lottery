package me.zohar.lottery.issue.service;

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
import me.zohar.lottery.issue.domain.LotterySituation;
import me.zohar.lottery.issue.param.LotterySituationQueryCondParam;
import me.zohar.lottery.issue.repo.LotterySituationRepo;
import me.zohar.lottery.issue.vo.LotterySituationVO;

@Service
public class LotterySituationService {

	@Autowired
	private LotterySituationRepo lotterySituationRepo;

	public LotterySituationVO findLotterySituationById(String id) {
		return LotterySituationVO.convertFor(lotterySituationRepo.getOne(id));
	}

	/**
	 * 分页获取开奖情况
	 * 
	 * @param param
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageResult<LotterySituationVO> findLotterySituationByPage(LotterySituationQueryCondParam param) {
		Specification<LotterySituation> spec = new Specification<LotterySituation>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<LotterySituation> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getGameCode())) {
					predicates.add(builder.equal(root.get("gameCode"), param.getGameCode()));
				}
				if (param.getIssueNum() != null) {
					predicates.add(builder.equal(root.get("issueNum"), param.getIssueNum()));
				}
				if (StrUtil.isNotEmpty(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (param.getLotteryStartDate() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("lotteryDate").as(Date.class),
							DateUtil.beginOfDay(param.getLotteryStartDate())));
				}
				if (param.getLotteryEndDate() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("lotteryDate").as(Date.class),
							DateUtil.endOfDay(param.getLotteryEndDate())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<LotterySituation> result = lotterySituationRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.asc("startTime"))));
		PageResult<LotterySituationVO> pageResult = new PageResult<>(LotterySituationVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
