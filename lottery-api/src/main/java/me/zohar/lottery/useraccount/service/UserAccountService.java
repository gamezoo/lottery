package me.zohar.lottery.useraccount.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizErrorCode;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.useraccount.domain.AccountChangeLog;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.param.AccountChangeLogQueryCondParam;
import me.zohar.lottery.useraccount.param.BindBankInfoParam;
import me.zohar.lottery.useraccount.param.ModifyLoginPwdParam;
import me.zohar.lottery.useraccount.param.ModifyMoneyPwdParam;
import me.zohar.lottery.useraccount.param.UserAccountRegisterParam;
import me.zohar.lottery.useraccount.repo.AccountChangeLogRepo;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;
import me.zohar.lottery.useraccount.vo.AccountChangeLogVO;
import me.zohar.lottery.useraccount.vo.BankInfoVO;
import me.zohar.lottery.useraccount.vo.UserAccountInfoVO;

@Service
public class UserAccountService {

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@ParamValid
	@Transactional(readOnly = false)
	public void bindBankInfo(BindBankInfoParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BeanUtils.copyProperties(param, userAccount);
		userAccount.setBankInfoLatelyModifyTime(new Date());
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional(readOnly = false)
	public void modifyLoginPwd(ModifyLoginPwdParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		if (!pwdEncoder.matches(param.getOldLoginPwd(), userAccount.getLoginPwd())) {
			throw new BizException(BizErrorCode.旧的登录密码不正确.getCode(), BizErrorCode.旧的登录密码不正确.getMsg());
		}
		String newLoginPwd = pwdEncoder.encode(param.getNewLoginPwd());
		userAccount.setLoginPwd(newLoginPwd);
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional(readOnly = false)
	public void modifyMoneyPwd(ModifyMoneyPwdParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		if (!pwdEncoder.matches(param.getOldMoneyPwd(), userAccount.getMoneyPwd())) {
			throw new BizException(BizErrorCode.旧的资金密码不正确.getCode(), BizErrorCode.旧的资金密码不正确.getMsg());
		}
		String newMoneyPwd = pwdEncoder.encode(param.getNewMoneyPwd());
		userAccount.setMoneyPwd(newMoneyPwd);
		userAccountRepo.save(userAccount);
	}

	@Transactional(readOnly = true)
	public UserAccountInfoVO getUserAccountInfo(String userAccountId) {
		return UserAccountInfoVO.convertFor(userAccountRepo.getOne(userAccountId));
	}

	@Transactional(readOnly = true)
	public BankInfoVO getBankInfo(String userAccountId) {
		return BankInfoVO.convertFor(userAccountRepo.getOne(userAccountId));
	}

	@Transactional
	public UserAccountInfoVO userAccountRegister(UserAccountRegisterParam param) {
		UserAccount userAccount = userAccountRepo.findByUserName(param.getUserName());
		if (userAccount != null) {
			throw new BizException(BizErrorCode.用户名已存在.getCode(), BizErrorCode.用户名已存在.getMsg());
		}
		String encodePwd = new BCryptPasswordEncoder().encode(param.getLoginPwd());
		param.setLoginPwd(encodePwd);
		UserAccount newUserAccount = param.convertToPo();
		newUserAccount.setBalance(100d);
		userAccountRepo.save(newUserAccount);
		return UserAccountInfoVO.convertFor(newUserAccount);
	}

	@Transactional(readOnly = true)
	public PageResult<AccountChangeLogVO> findMyAccountChangeLogByPage(AccountChangeLogQueryCondParam param) {
		Specification<AccountChangeLog> spec = new Specification<AccountChangeLog>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<AccountChangeLog> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getGameCode())) {
					predicates.add(builder.equal(root.get("gameCode"), param.getGameCode()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("accountChangeTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("accountChangeTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				if (StrUtil.isNotEmpty(param.getAccountChangeTypeCode())) {
					predicates.add(builder.equal(root.get("accountChangeTypeCode"), param.getAccountChangeTypeCode()));
				}
				if (StrUtil.isNotEmpty(param.getUserAccountId())) {
					predicates.add(builder.equal(root.get("userAccountId"), param.getUserAccountId()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<AccountChangeLog> result = accountChangeLogRepo.findAll(spec, PageRequest.of(param.getPageNum() - 1,
				param.getPageSize(), Sort.by(Sort.Order.desc("accountChangeTime"))));
		PageResult<AccountChangeLogVO> pageResult = new PageResult<>(AccountChangeLogVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
