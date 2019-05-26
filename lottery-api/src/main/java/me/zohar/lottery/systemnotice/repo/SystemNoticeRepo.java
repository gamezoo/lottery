package me.zohar.lottery.systemnotice.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.systemnotice.domain.SystemNotice;

public interface SystemNoticeRepo extends JpaRepository<SystemNotice, String>, JpaSpecificationExecutor<SystemNotice> {

	List<SystemNotice> findTop5ByPublishDateLessThanOrderByPublishDateDesc(Date publishDate);

}
