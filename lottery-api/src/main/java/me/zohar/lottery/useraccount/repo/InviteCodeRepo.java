package me.zohar.lottery.useraccount.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.useraccount.domain.InviteCode;

public interface InviteCodeRepo extends JpaRepository<InviteCode, String>, JpaSpecificationExecutor<InviteCode> {
	
	InviteCode findTopByUserAccountIdOrderByPeriodOfValidityDesc(String userAccountId);
	
	InviteCode findTopByCodeAndPeriodOfValidityGreaterThanEqual(String code, Date periodOfValidity);
	

}
