package me.zohar.lottery.game.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.game.domain.GamePlay;

public interface GamePlayRepo extends JpaRepository<GamePlay, String>, JpaSpecificationExecutor<GamePlay> {

	List<GamePlay> findByGameCodeOrderByOrderNo(String gameCode);

	GamePlay findByGameCodeAndGamePlayCode(String gameCode, String gamePlayCode);

}
