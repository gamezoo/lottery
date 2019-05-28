package me.zohar.lottery.information.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.information.domain.InformationCrawler;

public interface InformationCrawlerRepo
		extends JpaRepository<InformationCrawler, String>, JpaSpecificationExecutor<InformationCrawler> {

}
