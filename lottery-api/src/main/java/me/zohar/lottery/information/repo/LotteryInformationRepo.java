package me.zohar.lottery.information.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.information.domain.LotteryInformation;

public interface LotteryInformationRepo
		extends JpaRepository<LotteryInformation, String>, JpaSpecificationExecutor<LotteryInformation> {

	LotteryInformation findBytitleAndPublishTime(String title, Date publishTime);

	List<LotteryInformation> findTop13ByOrderByPublishTimeDesc();

}
