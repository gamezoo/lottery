package me.zohar.lottery.dictconfig.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.dictconfig.domain.DictItem;
import me.zohar.lottery.dictconfig.domain.DictType;
import me.zohar.lottery.dictconfig.repo.DictTypeRepo;
import me.zohar.lottery.dictconfig.vo.DictItemVO;

@Service
@Slf4j
public class DictService {

	@Autowired
	private DictTypeRepo dictTypeRepo;

	/**
	 * 同步数据字典到缓存
	 */
	@Transactional(readOnly = true)
	public void syncDictToCache() {
		log.warn("同步数据字典");
		DictHolder.clearDict();
		List<DictType> dictTypes = dictTypeRepo.findAll();
		for (DictType dictType : dictTypes) {
			Map<String, DictItemVO> dictItemMap = new LinkedHashMap<>();
			Set<DictItem> dictItems = dictType.getDictItems();
			for (DictItem dictItem : dictItems) {
				dictItemMap.put(dictItem.getDictItemCode(), DictItemVO.convertFor(dictItem));
			}
			DictHolder.putDict(dictType.getDictTypeCode(), dictItemMap);
		}
	}

}
