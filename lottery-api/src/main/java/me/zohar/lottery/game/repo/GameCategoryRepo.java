package me.zohar.lottery.game.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.game.domain.GameCategory;

public interface GameCategoryRepo extends JpaRepository<GameCategory, String>, JpaSpecificationExecutor<GameCategory> {

	List<GameCategory> findByOrderByOrderNo();
	
	GameCategory findByGameCategoryCode(String gameCategoryCode);

}
