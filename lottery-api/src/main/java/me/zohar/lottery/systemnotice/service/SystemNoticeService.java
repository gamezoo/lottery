package me.zohar.lottery.systemnotice.service;

import java.util.ArrayList;
import java.util.Date;
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
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.systemnotice.domain.SystemNotice;
import me.zohar.lottery.systemnotice.param.AddOrUpdateSystemNoticeParam;
import me.zohar.lottery.systemnotice.param.SystemNoticeQueryCondParam;
import me.zohar.lottery.systemnotice.repo.SystemNoticeRepo;
import me.zohar.lottery.systemnotice.vo.SystemNoticeVO;

@Service
public class SystemNoticeService {

	@Autowired
	private SystemNoticeRepo systemNoticeRepo;

	@Transactional(readOnly = true)
	public PageResult<SystemNoticeVO> findSystemNoticeByPage(SystemNoticeQueryCondParam param) {
		Specification<SystemNotice> spec = new Specification<SystemNotice>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<SystemNotice> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getNoticeTitle())) {
					predicates.add(builder.like(root.get("noticeTitle"), "%" + param.getNoticeTitle() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<SystemNotice> result = systemNoticeRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<SystemNoticeVO> pageResult = new PageResult<>(SystemNoticeVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@ParamValid
	@Transactional
	public void addOrUpdateSystemNotice(AddOrUpdateSystemNoticeParam param) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			SystemNotice systemNotice = param.convertToPo();
			systemNoticeRepo.save(systemNotice);
		}
		// 修改
		else {
			SystemNotice systemNotice = systemNoticeRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, systemNotice);
			systemNoticeRepo.save(systemNotice);
		}
	}

	@Transactional
	public void delSystemNoticeById(@NotBlank String id) {
		systemNoticeRepo.deleteById(id);
	}

	@Transactional(readOnly = true)
	public SystemNoticeVO findSystemNoticeById(@NotBlank String id) {
		return SystemNoticeVO.convertFor(systemNoticeRepo.getOne(id));
	}

	public List<SystemNoticeVO> findTop5PublishedSystemNotice() {
		return SystemNoticeVO
				.convertFor(systemNoticeRepo.findTop5ByPublishDateLessThanOrderByPublishDateDesc(new Date()));
	}

}
