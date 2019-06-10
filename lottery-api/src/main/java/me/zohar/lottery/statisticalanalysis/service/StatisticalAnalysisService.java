package me.zohar.lottery.statisticalanalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.statisticalanalysis.param.AccountProfitAndLossQueryCondParam;
import me.zohar.lottery.statisticalanalysis.vo.AccountProfitAndLossVO;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;

@Service
public class StatisticalAnalysisService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Transactional(readOnly = true)
	public PageResult<AccountProfitAndLossVO> findAccountProfitAndLossByPage(AccountProfitAndLossQueryCondParam param) {
		UserAccount currentAccount = userAccountRepo.getOne(param.getCurrentAccountId());
		UserAccount lowerLevelAccount = currentAccount;
		if (StrUtil.isNotBlank(param.getUserName())) {
			lowerLevelAccount = userAccountRepo.findByUserName(param.getUserName());
			if (lowerLevelAccount == null) {
				throw new BizException(BizError.用户名不存在);
			}
			// 说明该用户名对应的账号不是当前账号的下级账号
			if (!lowerLevelAccount.getAccountLevelPath().startsWith(currentAccount.getAccountLevelPath())) {
				throw new BizException(BizError.不是上级账号无权查看该账号及下级的盈亏记录);
			}
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("accountLevelPath", lowerLevelAccount.getAccountLevelPath() + "%");

		StringBuilder select = new StringBuilder();
		select.append(" select t.userAccountId,t.userName,t.accountType,t.accountLevel,t.accountLevelPath,t.balance, ");
		select.append(" SUM(t.rechargeAmount) as rechargeAmount,SUM(t.withdrawAmount) as withdrawAmount, ");
		select.append(
				" SUM(t.totalBettingAmount) as totalBettingAmount,SUM(t.totalWinningAmount) as totalWinningAmount, ");
		select.append(" SUM(t.rebateAmount) as rebateAmount,SUM(t.lowerLevelRebateAmount) as lowerLevelRebateAmount, ");
		select.append(" SUM(t.bettingProfitAndLoss) as bettingProfitAndLoss ");
		select.append(" from EverydayProfitAndLoss t ");
		StringBuilder groupBy = new StringBuilder(
				" group by t.userAccountId,t.userName,t.accountType,t.accountLevel,t.accountLevelPath,t.balance ");
		StringBuilder where = new StringBuilder(" where t.accountLevelPath like :accountLevelPath ");
		if (StrUtil.isNotBlank(param.getAccountType())) {
			where.append(" and t.accountType = :accountType ");
			paramMap.put("accountType", param.getAccountType());
		}
		if (param.getStartTime() != null) {
			where.append(" and t.everyday >= :startTime ");
			paramMap.put("startTime", DateUtil.beginOfDay(param.getStartTime()));
		}
		if (param.getEndTime() != null) {
			where.append(" and t.everyday <= :endTime ");
			paramMap.put("endTime", DateUtil.endOfDay(param.getEndTime()));
		}
		String sql = select.append(where).append(groupBy).toString();

		TypedQuery<Object[]> query = em.createQuery(sql, Object[].class);
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		long total = query.getResultList().size();

		query = em.createQuery(sql, Object[].class);
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		query.setFirstResult((param.getPageNum() - 1) * param.getPageSize());
		query.setMaxResults(param.getPageSize());
		List<Object[]> result = query.getResultList();
		PageResult<AccountProfitAndLossVO> pageResult = new PageResult<>(AccountProfitAndLossVO.convertFor(result),
				param.getPageNum(), param.getPageSize(), total);
		return pageResult;
	}

}
