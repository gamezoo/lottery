package me.zohar.lottery.rechargewithdraw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.NumberUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.rechargewithdraw.domain.WithdrawRecord;
import me.zohar.lottery.rechargewithdraw.param.StartWithdrawParam;
import me.zohar.lottery.rechargewithdraw.repo.WithdrawRecordRepo;
import me.zohar.lottery.useraccount.domain.AccountChangeLog;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.repo.AccountChangeLogRepo;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;

@Service
public class WithdrawService {

	@Autowired
	private WithdrawRecordRepo withdrawRecordRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;
	
	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@ParamValid
	@Transactional
	public void startWithdraw(StartWithdrawParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		if (!new BCryptPasswordEncoder().matches(param.getMoneyPwd(), userAccount.getMoneyPwd())) {
			throw new BizException(BizError.资金密码不正确);
		}
		double balance = NumberUtil.round(userAccount.getBalance() - param.getWithdrawAmount(), 4).doubleValue();
		if (balance < 0) {
			throw new BizException(BizError.余额不足);
		}
		if (userAccount.getBankInfoLatelyModifyTime() == null) {
			throw new BizException(BizError.银行卡未绑定无法进行提现);
		}
		
		WithdrawRecord withdrawRecord = param.convertToPo();
		withdrawRecord.setBankInfo(userAccount);
		withdrawRecordRepo.save(withdrawRecord);
		
		userAccount.setBalance(balance);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithStartWithdraw(userAccount, withdrawRecord));
	}

}
