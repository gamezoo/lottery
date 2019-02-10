package me.zohar.lottery.game.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.game.domain.Game;

public interface GameRepo extends JpaRepository<Game, String>, JpaSpecificationExecutor<Game> {

}
