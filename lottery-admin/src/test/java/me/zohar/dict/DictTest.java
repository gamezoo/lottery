package me.zohar.dict;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.dictconfig.domain.DictItem;
import me.zohar.lottery.dictconfig.domain.DictType;
import me.zohar.lottery.dictconfig.repo.DictItemRepo;
import me.zohar.lottery.dictconfig.repo.DictTypeRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DictTest {

	@Autowired
	private DictTypeRepo dictTypeRepo;

	@Autowired
	private DictItemRepo dictItemRepo;

	public DictItem buildDictItem(String dictItemCode, String dictItemName, Double orderNo, String dictTypeId) {
		DictItem dictItem = new DictItem();
		dictItem.setId(IdUtils.getId());
		dictItem.setDictItemCode(dictItemCode);
		dictItem.setDictItemName(dictItemName);
		dictItem.setOrderNo(orderNo);
		dictItem.setDictTypeId(dictTypeId);
		return dictItem;
	}

	/**
	 * 初始化提现记录状态字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initWithdrawRecordStateDict() {
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("withdrawRecordState");
		dictType.setDictTypeName("提现记录状态");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "发起提现", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("2", "审核通过", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("3", "审核不通过", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("4", "已到帐", 2d, dictType.getId()));
	}

	/**
	 * 初始化充值方式字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initRechargeWayDict() {
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("rechargeWay");
		dictType.setDictTypeName("充值方式");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("zfbsm", "支付宝扫码", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("wxsm", "微信扫码", 2d, dictType.getId()));
	}

	/**
	 * 初始化账号状态字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initAccountStateDict() {
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("accountState");
		dictType.setDictTypeName("账号状态");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "启用", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("0", "禁用", 2d, dictType.getId()));
	}

	/**
	 * 初始化期号状态字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initIssueStateDict() {
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("issueState");
		dictType.setDictTypeName("期号状态");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "未开奖", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("2", "已开奖", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("3", "已结算", 3d, dictType.getId()));
		dictItemRepo.save(buildDictItem("4", "已作废", 4d, dictType.getId()));
	}

	/**
	 * 初始化游戏玩法状态字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initGamePlayStateDict() {
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("gamePlayState");
		dictType.setDictTypeName("玩法状态");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "启用", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("0", "禁用", 2d, dictType.getId()));
	}

	/**
	 * 初始化游戏状态字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initGameStateDict() {
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("gameState");
		dictType.setDictTypeName("游戏状态");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "启用", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("2", "禁用", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("3", "维护中", 3d, dictType.getId()));
	}

	/**
	 * 充提日志订单类型字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initRechargeWithdrawLogOrderTypeDict() {
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("rechargeWithdrawLogOrderType");
		dictType.setDictTypeName("充值日志订单类型");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "充值", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("2", "提现", 1d, dictType.getId()));
	}

	/**
	 * 初始化充值订单状态字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initRechargeOrderStateDict() {
		deleteDict("rechargeOrderState");
		
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("rechargeOrderState");
		dictType.setDictTypeName("充值订单状态");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "待支付", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("2", "已支付", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("3", "已结算", 3d, dictType.getId()));
		dictItemRepo.save(buildDictItem("4", "超时取消", 4d, dictType.getId()));
		dictItemRepo.save(buildDictItem("5", "人工取消", 5d, dictType.getId()));

	}

	/**
	 * 初始化投注订单状态字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initbettingOrderStateDict() {
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("bettingOrderState");
		dictType.setDictTypeName("投注订单状态");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "未开奖", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("2", "未中奖", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("3", "已中奖", 3d, dictType.getId()));
	}

	/**
	 * 初始化游戏字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initGameDict() {
		deleteDict("game");

		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("game");
		dictType.setDictTypeName("游戏");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("CQSSC", "重庆时时彩", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("XJSSC", "新疆时时彩", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("JX11X5", "江西11选5", 2d, dictType.getId()));
	}

	public void deleteDict(String dictTypeCode) {
		DictType dictType = dictTypeRepo.findByDictTypeCode(dictTypeCode);
		if (dictType == null) {
			return;
		}
		for (DictItem dictItem : dictType.getDictItems()) {
			dictItemRepo.delete(dictItem);
		}
		dictTypeRepo.delete(dictType);
	}

	/**
	 * 初始化账变类型字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initAccountChangeTypeDict() {
		deleteDict("accountChangeType");
		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("accountChangeType");
		dictType.setDictTypeName("账变类型");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("1", "账号充值", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("2", "充值优惠", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("3", "投注扣款", 3d, dictType.getId()));
		dictItemRepo.save(buildDictItem("4", "投注返奖", 4d, dictType.getId()));
		dictItemRepo.save(buildDictItem("5", "账号提现", 5d, dictType.getId()));
		dictItemRepo.save(buildDictItem("6", "活动礼金", 6d, dictType.getId()));
	}

	/**
	 * 初始化账号类型字典
	 */
	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void initAccountTypeDict() {
		deleteDict("accountType");

		DictType dictType = new DictType();
		dictType.setId(IdUtils.getId());
		dictType.setDictTypeCode("accountType");
		dictType.setDictTypeName("账号类型");
		dictTypeRepo.save(dictType);

		dictItemRepo.save(buildDictItem("admin", "管理员", 1d, dictType.getId()));
		dictItemRepo.save(buildDictItem("agent", "代理", 2d, dictType.getId()));
		dictItemRepo.save(buildDictItem("member", "会员", 3d, dictType.getId()));
	}

}
