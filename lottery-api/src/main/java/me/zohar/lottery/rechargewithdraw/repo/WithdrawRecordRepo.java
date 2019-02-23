package me.zohar.lottery.rechargewithdraw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.rechargewithdraw.domain.WithdrawRecord;

public interface WithdrawRecordRepo
		extends JpaRepository<WithdrawRecord, String>, JpaSpecificationExecutor<WithdrawRecord> {

}
