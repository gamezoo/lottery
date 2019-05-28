package me.zohar.lottery.information.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.StrUtil;
import groovy.lang.GroovyShell;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.information.domain.InformationCrawler;
import me.zohar.lottery.information.domain.LotteryInformation;
import me.zohar.lottery.information.param.AddOrUpdateInformationCrawlerParam;
import me.zohar.lottery.information.param.LotteryInformationQueryCondParam;
import me.zohar.lottery.information.param.SyncInformationParam;
import me.zohar.lottery.information.repo.InformationCrawlerRepo;
import me.zohar.lottery.information.repo.LotteryInformationRepo;
import me.zohar.lottery.information.vo.LotteryInformationCrawlerVO;
import me.zohar.lottery.information.vo.LotteryInformationVO;

@Service
public class LotteryInformationService {

	@Autowired
	private LotteryInformationRepo lotteryInformationRepo;

	@Autowired
	private InformationCrawlerRepo informationCrawlerRepo;

	public LotteryInformationVO findInformationById(String id) {
		return LotteryInformationVO.convertFor(lotteryInformationRepo.getOne(id));
	}

	public List<LotteryInformationVO> findTop13Information() {
		return LotteryInformationVO.convertFor(lotteryInformationRepo.findTop13ByOrderByPublishTimeDesc());
	}

	public void syncInformation(List<SyncInformationParam> params) {
		for (SyncInformationParam param : params) {
			LotteryInformation existLotteryInformation = lotteryInformationRepo
					.findBytitleAndCreateTime(param.getTitle(), param.getCreateTime());
			if (existLotteryInformation != null) {
				continue;
			}
			LotteryInformation lotteryInformation = param.convertToPo();
			lotteryInformationRepo.save(lotteryInformation);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<LotteryInformationVO> collectionInformation(@NotBlank String id) {
		InformationCrawler crawler = informationCrawlerRepo.getOne(id);
		GroovyShell shell = new GroovyShell();
		List<LotteryInformationVO> vos = (List<LotteryInformationVO>) shell.evaluate(crawler.getScript());
		for (LotteryInformationVO vo : vos) {
			vo.setSource(crawler.getSource());
		}
		return vos;
	}

	@Transactional(readOnly = true)
	public LotteryInformationCrawlerVO findInformationCrawlerById(@NotBlank String id) {
		return LotteryInformationCrawlerVO.convertFor(informationCrawlerRepo.getOne(id));
	}

	@Transactional
	public void delInformationCrawlerById(@NotBlank String id) {
		informationCrawlerRepo.deleteById(id);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateInformationCrawler(AddOrUpdateInformationCrawlerParam param) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			InformationCrawler informationCrawler = param.convertToPo();
			informationCrawlerRepo.save(informationCrawler);
		}
		// 修改
		else {
			InformationCrawler informationCrawler = informationCrawlerRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, informationCrawler);
			informationCrawlerRepo.save(informationCrawler);
		}
	}

	@Transactional(readOnly = true)
	public List<LotteryInformationCrawlerVO> findAllInformationCrawler() {
		return LotteryInformationCrawlerVO.convertFor(informationCrawlerRepo.findAll());
	}

	@Transactional(readOnly = true)
	public PageResult<LotteryInformationVO> findLotteryInformationByPage(LotteryInformationQueryCondParam param) {
		Specification<LotteryInformation> spec = new Specification<LotteryInformation>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<LotteryInformation> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getTitle())) {
					predicates.add(builder.like(root.get("title"), "%" + param.getTitle() + "%"));
				}
				if (StrUtil.isNotBlank(param.getSource())) {
					predicates.add(builder.like(root.get("source"), "%" + param.getSource() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<LotteryInformation> result = lotteryInformationRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<LotteryInformationVO> pageResult = new PageResult<>(
				LotteryInformationVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

}
