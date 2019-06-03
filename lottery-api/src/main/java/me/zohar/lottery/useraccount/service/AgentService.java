package me.zohar.lottery.useraccount.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import me.zohar.lottery.useraccount.domain.RebateAndOdds;
import me.zohar.lottery.useraccount.repo.RebateAndOddsRepo;
import me.zohar.lottery.useraccount.vo.RebateAndOddsVO;

@Validated
@Service
public class AgentService {

	@Autowired
	private RebateAndOddsRepo rebateAndOddsRepo;

	@Transactional(readOnly = true)
	public List<RebateAndOddsVO> findAllRebateAndOdds() {
		List<RebateAndOdds> rebateAndOddses = rebateAndOddsRepo.findAll(Sort.by(Sort.Order.desc("rebate")));
		return RebateAndOddsVO.convertFor(rebateAndOddses);
	}

}
