package me.zohar.lottery.dictconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zohar.lottery.dictconfig.vo.DictItemVO;

public class DictHolder {

	public static Map<String, Map<String, DictItemVO>> dictMap = new HashMap<>();

	public static void clearDict() {
		dictMap.clear();
	}

	public static void putDict(String key, Map<String, DictItemVO> dictItemMap) {
		dictMap.put(key, dictItemMap);
	}

	public static String getDictItemName(String dictTypeCode, String dictItemCode) {
		Map<String, DictItemVO> dictItemMap = dictMap.get(dictTypeCode);
		if (dictItemMap == null) {
			return null;
		}
		DictItemVO dictItemVO = dictItemMap.get(dictItemCode);
		if (dictItemVO == null) {
			return null;
		}
		return dictItemVO.getDictItemName();
	}

	public static List<DictItemVO> findDictItem(String dictTypeCode) {
		Map<String, DictItemVO> dictItemMap = dictMap.get(dictTypeCode);
		if (dictItemMap == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(dictItemMap.values());
	}

}
