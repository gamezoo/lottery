package me.zohar.lottery.game.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.game.domain.NumLocate;

public interface NumLocateRepo extends JpaRepository<NumLocate, String>, JpaSpecificationExecutor<NumLocate> {
	
	List<NumLocate> findByGamePlayId(String gamePlayId);

}
