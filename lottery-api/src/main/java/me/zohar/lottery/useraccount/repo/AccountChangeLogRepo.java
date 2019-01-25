package me.zohar.lottery.useraccount.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.useraccount.domain.AccountChangeLog;

public interface AccountChangeLogRepo
		extends JpaRepository<AccountChangeLog, String>, JpaSpecificationExecutor<AccountChangeLog> {

}
