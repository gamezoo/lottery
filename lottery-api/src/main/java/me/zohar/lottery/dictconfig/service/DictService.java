package me.zohar.lottery.dictconfig.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alicp.jetcache.anno.Cached;

import me.zohar.lottery.dictconfig.repo.DictItemRepo;
import me.zohar.lottery.dictconfig.vo.DictItemVO;

@Service
public class DictService {

	@Autowired
	private DictItemRepo dictItemRepo;

	@Cached(name = "dictItem_", key = "#dictTypeCode + '_' +  #dictItemCode", expire = 3600)
	@Transactional(readOnly = true)
	public DictItemVO findDictItemByDictTypeCodeAndDictItemCode(String dictTypeCode, String dictItemCode) {
		return DictItemVO
				.convertFor(dictItemRepo.findByDictTypeDictTypeCodeAndDictItemCode(dictTypeCode, dictItemCode));
	}

	@Cached(name = "dictItems_", key = "#dictTypeCode", expire = 3600)
	@Transactional(readOnly = true)
	public List<DictItemVO> findDictItemByDictTypeCode(String dictTypeCode) {
		return DictItemVO.convertFor(dictItemRepo.findByDictTypeDictTypeCodeOrderByOrderNo(dictTypeCode));
	}

}
