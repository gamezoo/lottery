package me.zohar.lottery.useraccount.service;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.mastercontrol.domain.InviteRegisterSetting;
import me.zohar.lottery.mastercontrol.domain.RegisterAmountSetting;
import me.zohar.lottery.mastercontrol.repo.InviteRegisterSettingRepo;
import me.zohar.lottery.mastercontrol.repo.RegisterAmountSettingRepo;
import me.zohar.lottery.useraccount.domain.AccountChangeLog;
import me.zohar.lottery.useraccount.domain.InviteCode;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.param.AccountChangeLogQueryCondParam;
import me.zohar.lottery.useraccount.param.AddUserAccountParam;
import me.zohar.lottery.useraccount.param.BindBankInfoParam;
import me.zohar.lottery.useraccount.param.ModifyLoginPwdParam;
import me.zohar.lottery.useraccount.param.ModifyMoneyPwdParam;
import me.zohar.lottery.useraccount.param.UserAccountEditParam;
import me.zohar.lottery.useraccount.param.UserAccountQueryCondParam;
import me.zohar.lottery.useraccount.param.UserAccountRegisterParam;
import me.zohar.lottery.useraccount.repo.AccountChangeLogRepo;
import me.zohar.lottery.useraccount.repo.InviteCodeRepo;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;
import me.zohar.lottery.useraccount.vo.AccountChangeLogVO;
import me.zohar.lottery.useraccount.vo.BankInfoVO;
import me.zohar.lottery.useraccount.vo.InviteDetailsInfoVO;
import me.zohar.lottery.useraccount.vo.LoginAccountInfoVO;
import me.zohar.lottery.useraccount.vo.UserAccountDetailsInfoVO;
import me.zohar.lottery.useraccount.vo.UserAccountInfoVO;

@Validated
@Service
public class UserAccountService {

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@Autowired
	private InviteCodeRepo inviteCodeRepo;

	@Autowired
	private RegisterAmountSettingRepo registerAmountSettingRepo;

	@Autowired
	private InviteRegisterSettingRepo inviteRegisterSettingRepo;

	/**
	 * 更新最近登录时间
	 */
	@Transactional
	public void updateLatelyLoginTime(String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		userAccount.setLatelyLoginTime(new Date());
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional
	public void updateUserAccount(UserAccountEditParam param) {
		UserAccount existUserAccount = userAccountRepo.findByUserName(param.getUserName());
		if (existUserAccount != null && !existUserAccount.getId().equals(param.getUserAccountId())) {
			throw new BizException(BizError.用户名已存在);
		}

		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BeanUtils.copyProperties(param, userAccount);
		userAccountRepo.save(userAccount);
	}

	@Transactional(readOnly = true)
	public UserAccountDetailsInfoVO findUserAccountDetailsInfoById(String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		return UserAccountDetailsInfoVO.convertFor(userAccount);
	}

	@Transactional(readOnly = true)
	public PageResult<UserAccountDetailsInfoVO> findUserAccountDetailsInfoByPage(UserAccountQueryCondParam param) {
		Specification<UserAccount> spec = new Specification<UserAccount>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<UserAccount> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getUserName())) {
					predicates.add(builder.like(root.get("userName"), "%" + param.getUserName() + "%"));
				}
				if (StrUtil.isNotEmpty(param.getRealName())) {
					predicates.add(builder.like(root.get("realName"), "%" + param.getRealName() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<UserAccount> result = userAccountRepo.findAll(spec, PageRequest.of(param.getPageNum() - 1,
				param.getPageSize(), Sort.by(Sort.Order.desc("registeredTime"))));
		PageResult<UserAccountDetailsInfoVO> pageResult = new PageResult<>(
				UserAccountDetailsInfoVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@ParamValid
	@Transactional
	public void bindBankInfo(BindBankInfoParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BeanUtils.copyProperties(param, userAccount);
		userAccount.setBankInfoLatelyModifyTime(new Date());
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional
	public void modifyLoginPwd(ModifyLoginPwdParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		if (!pwdEncoder.matches(param.getOldLoginPwd(), userAccount.getLoginPwd())) {
			throw new BizException(BizError.旧的登录密码不正确);
		}
		modifyLoginPwd(param.getUserAccountId(), param.getNewLoginPwd());
	}

	@ParamValid
	@Transactional
	public void modifyLoginPwd(@NotBlank String userAccountId, @NotBlank String newLoginPwd) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		userAccount.setLoginPwd(new BCryptPasswordEncoder().encode(newLoginPwd));
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional
	public void modifyMoneyPwd(ModifyMoneyPwdParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		if (!pwdEncoder.matches(param.getOldMoneyPwd(), userAccount.getMoneyPwd())) {
			throw new BizException(BizError.旧的资金密码不正确);
		}
		String newMoneyPwd = pwdEncoder.encode(param.getNewMoneyPwd());
		userAccount.setMoneyPwd(newMoneyPwd);
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional
	public void modifyMoneyPwd(@NotBlank String userAccountId, @NotBlank String newMoneyPwd) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		userAccount.setMoneyPwd(new BCryptPasswordEncoder().encode(newMoneyPwd));
		userAccountRepo.save(userAccount);
	}

	@Transactional(readOnly = true)
	public LoginAccountInfoVO getLoginAccountInfo(String userName) {
		return LoginAccountInfoVO.convertFor(userAccountRepo.findByUserName(userName));
	}

	@Transactional(readOnly = true)
	public UserAccountInfoVO getUserAccountInfo(String userAccountId) {
		return UserAccountInfoVO.convertFor(userAccountRepo.getOne(userAccountId));
	}

	@Transactional(readOnly = true)
	public BankInfoVO getBankInfo(String userAccountId) {
		return BankInfoVO.convertFor(userAccountRepo.getOne(userAccountId));
	}

	@ParamValid
	@Transactional
	public void addUserAccount(AddUserAccountParam param) {
		UserAccount userAccount = userAccountRepo.findByUserName(param.getUserName());
		if (userAccount != null) {
			throw new BizException(BizError.用户名已存在);
		}
		String encodePwd = new BCryptPasswordEncoder().encode(param.getLoginPwd());
		param.setLoginPwd(encodePwd);
		UserAccount newUserAccount = param.convertToPo();
		newUserAccount.setBalance(100d);
		if (StrUtil.isNotBlank(param.getInviterUserName())) {
			UserAccount inviter = userAccountRepo.findByUserName(param.getInviterUserName());
			if (inviter == null) {
				throw new BizException(BizError.邀请人不存在);
			}
			newUserAccount.setInviterId(inviter.getId());
		}
		userAccountRepo.save(newUserAccount);
		updateBalanceWithRegisterAmount(newUserAccount);
	}

	/**
	 * 新注册用户送活动礼金
	 */
	@Transactional
	public void updateBalanceWithRegisterAmount(UserAccount userAccount) {
		RegisterAmountSetting setting = registerAmountSettingRepo.findTopByOrderByEnabled();
		if (setting == null || !setting.getEnabled()) {
			return;
		}
		double balance = NumberUtil.round(userAccount.getBalance() + setting.getRegisterAmount(), 4).doubleValue();
		userAccount.setBalance(balance);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithRegisterAmount(userAccount, setting));
	}

	@Transactional
	public UserAccountInfoVO userAccountRegister(UserAccountRegisterParam param) {
		UserAccount userAccount = userAccountRepo.findByUserName(param.getUserName());
		if (userAccount != null) {
			throw new BizException(BizError.用户名已存在);
		}
		String encodePwd = new BCryptPasswordEncoder().encode(param.getLoginPwd());
		param.setLoginPwd(encodePwd);
		UserAccount newUserAccount = param.convertToPo();
		newUserAccount.setBalance(0d);
		newUserAccount.setInviterId(confirmCodeAndGetInviterId(param.getInviteCode()));
		userAccountRepo.save(newUserAccount);
		updateBalanceWithRegisterAmount(newUserAccount);
		return UserAccountInfoVO.convertFor(newUserAccount);
	}

	/**
	 * 确认邀请码并返回邀请人id
	 * 
	 * @param code
	 * @return
	 */
	public String confirmCodeAndGetInviterId(String code) {
		InviteRegisterSetting setting = inviteRegisterSettingRepo.findTopByOrderByEnabled();
		if (setting == null || !setting.getEnabled()) {
			return null;
		}
		if (StrUtil.isBlank(code)) {
			throw new BizException(BizError.邀请码不存在或已失效);
		}
		InviteCode inviteCode = inviteCodeRepo.findTopByCodeAndPeriodOfValidityGreaterThanEqual(code, new Date());
		if (inviteCode == null) {
			throw new BizException(BizError.邀请码不存在或已失效);
		}
		return inviteCode.getUserAccountId();
	}

	@Transactional(readOnly = true)
	public PageResult<AccountChangeLogVO> findAccountChangeLogByPage(AccountChangeLogQueryCondParam param) {
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
				param.getPageSize(), Sort.by(Sort.Order.desc("accountChangeTime"), Sort.Order.desc("issueNum"))));
		PageResult<AccountChangeLogVO> pageResult = new PageResult<>(AccountChangeLogVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public InviteDetailsInfoVO getInviteDetailsInfo(String userAccountId) {
		InviteCode inviteCode = inviteCodeRepo.findTopByUserAccountIdOrderByPeriodOfValidityDesc(userAccountId);
		if (inviteCode == null) {
			return null;
		}

		Long numberOfInvite = userAccountRepo.countByInviterId(userAccountId);
		InviteDetailsInfoVO inviteDetailsInfo = InviteDetailsInfoVO.convertFor(inviteCode, numberOfInvite);
		return inviteDetailsInfo;
	}

	/**
	 * 生成邀请码
	 * 
	 * @param userAccountId
	 */
	@Transactional
	public void generateInviteCode(String userAccountId) {
		InviteRegisterSetting setting = inviteRegisterSettingRepo.findTopByOrderByEnabled();
		if (setting == null || !setting.getEnabled()) {
			throw new BizException(BizError.邀请注册功能已关闭);
		}

		InviteCode inviteCode = inviteCodeRepo.findTopByUserAccountIdOrderByPeriodOfValidityDesc(userAccountId);
		if (inviteCode != null && inviteCode.getPeriodOfValidity().getTime() > new Date().getTime()) {
			return;
		}

		String code = IdUtil.fastSimpleUUID().substring(0, 6);
		while (inviteCodeRepo.findTopByCodeAndPeriodOfValidityGreaterThanEqual(code, new Date()) != null) {
			code = IdUtil.fastSimpleUUID().substring(0, 6);
		}
		InviteCode newInviteCode = InviteCode.generateInviteCode(code, setting.getEffectiveDuration(), userAccountId);
		inviteCodeRepo.save(newInviteCode);
	}

	@ParamValid
	@Transactional
	public void delUserAccount(@NotBlank String userAccountId) {
		userAccountRepo.deleteById(userAccountId);
	}

}
