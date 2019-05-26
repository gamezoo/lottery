package me.zohar.lottery.dictconfig.service;

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

import com.alicp.jetcache.anno.Cached;

import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.dictconfig.domain.ConfigItem;
import me.zohar.lottery.dictconfig.param.ConfigItemQueryCondParam;
import me.zohar.lottery.dictconfig.param.ConfigParam;
import me.zohar.lottery.dictconfig.repo.ConfigItemRepo;
import me.zohar.lottery.dictconfig.vo.ConfigItemVO;

@Service
public class ConfigService {

	@Autowired
	private ConfigItemRepo configItemRepo;

	@Transactional(readOnly = true)
	public PageResult<ConfigItemVO> findConfigItemByPage(ConfigItemQueryCondParam param) {
		Specification<ConfigItem> spec = new Specification<ConfigItem>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<ConfigItem> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getConfigCode())) {
					predicates.add(builder.like(root.get("configCode"), "%" + param.getConfigCode() + "%"));
				}
				if (StrUtil.isNotBlank(param.getConfigName())) {
					predicates.add(builder.like(root.get("configName"), "%" + param.getConfigName() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<ConfigItem> result = configItemRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("configCode"))));
		PageResult<ConfigItemVO> pageResult = new PageResult<>(ConfigItemVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Cached(name = "configItem_", key = "#configCode", expire = 3600)
	@Transactional(readOnly = true)
	public ConfigItemVO findConfigItemByConfigCode(String configCode) {
		return ConfigItemVO.convertFor(configItemRepo.findByConfigCode(configCode));
	}

	@ParamValid
	@Transactional(readOnly = true)
	public ConfigItemVO findConfigItemById(@NotBlank String id) {
		return ConfigItemVO.convertFor(configItemRepo.getOne(id));
	}

	@ParamValid
	@Transactional
	public void addOrUpdateConfig(ConfigParam configParam) {
		// 新增
		if (StrUtil.isBlank(configParam.getId())) {
			ConfigItem configItem = configParam.convertToPo();
			configItemRepo.save(configItem);
		}
		// 修改
		else {
			ConfigItem configItem = configItemRepo.getOne(configParam.getId());
			BeanUtils.copyProperties(configParam, configItem);
			configItemRepo.save(configItem);
		}
	}

	@Transactional
	public void delConfigById(@NotBlank String id) {
		configItemRepo.deleteById(id);
	}

}
