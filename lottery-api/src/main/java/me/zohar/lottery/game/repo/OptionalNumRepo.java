package me.zohar.lottery.game.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.game.domain.OptionalNum;

public interface OptionalNumRepo extends JpaRepository<OptionalNum, String>, JpaSpecificationExecutor<OptionalNum> {

}
