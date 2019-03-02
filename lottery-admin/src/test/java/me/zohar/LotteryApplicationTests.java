package me.zohar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.IdUtil;
import me.zohar.lottery.game.domain.GamePlay;
import me.zohar.lottery.game.domain.NumLocate;
import me.zohar.lottery.game.repo.GamePlayRepo;
import me.zohar.lottery.game.repo.NumLocateRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryApplicationTests {

	@Autowired
	private GamePlayRepo gamePlayRepo;

	@Autowired
	private NumLocateRepo numLocateRepo;

	public GamePlay buildGamePaly() {
		GamePlay gamePlay = new GamePlay();
		gamePlay.setId(IdUtil.objectId());
		gamePlay.setGameCode("CQSSC");
		return gamePlay;
	}

	public NumLocate buildNumLocate(String gamePlayId, String numLocateName, String nums, Double orderNo) {
		NumLocate numLocate = new NumLocate();
		numLocate.setId(IdUtil.objectId());
		numLocate.setNumLocateName(numLocateName);
		numLocate.setNums(nums);
		numLocate.setMaxSelected(10);
		numLocate.setHasFilterBtnFlag(true);
		numLocate.setOrderNo(orderNo);
		numLocate.setGamePlayId(gamePlayId);
		return numLocate;
	}

	@Test
	@Transactional(readOnly = false)
	@Rollback(false)
	public void contextLoads() {
		String nums = "0,1,2,3,4,5,6,7,8,9";
		// numLocateRepo.save(buildNumLocate("5c36287bbe5a3239c6e895ba", "万位", nums,
		// 1d));
		// numLocateRepo.save(buildNumLocate("5c36287bbe5a3239c6e895ba", "千位", nums,
		// 2d));
		// numLocateRepo.save(buildNumLocate("5c36287bbe5a3239c6e895ba", "百位", nums,
		// 3d));
		// numLocateRepo.save(buildNumLocate("5c36287bbe5a3239c6e895ba", "十位", nums,
		// 4d));
		// numLocateRepo.save(buildNumLocate("5c36287bbe5a3239c6e895ba", "个位", nums,
		// 5d));

		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b7c", "万位", nums,
		// 1d));
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b7c", "千位", nums,
		// 2d));
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b7c", "百位", nums,
		// 3d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b7d", "前三和值",
		// "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27",
		// 1d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b7e", "组三", nums,
		// 1d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b7f", "组六", nums,
		// 1d));

		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b81", "千位", nums,
		// 1d));
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b81", "百位", nums,
		// 2d));
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b81", "十位", nums,
		// 3d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b82", "中三和值",
		// "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27",
		// 1d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b83", "组三", nums,
		// 1d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b84", "组六", nums,
		// 1d));

		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b86", "百位", nums,
		// 1d));
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b86", "十位", nums,
		// 2d));
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b86", "个位", nums,
		// 3d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b87", "三星和值",
		// "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27",
		// 1d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b88", "组三", nums,
		// 1d));
		//
		// numLocateRepo.save(buildNumLocate("5c362aa0be5a0dbf19264b89", "组六", nums,
		// 1d));

		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67aec", "十位", nums,
		// 1d));
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67aec", "个位", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67aed", "二星和值",
		// "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18", 1d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67aee", "二组", nums,
		// 2d));

		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67aef", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67af0", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67af1", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67af2", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67af3", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67af4", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67af5", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67af6", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362dcdbe5a8bafa2d67af7", "胆码", nums,
		// 2d));
		//
		// numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630be", "胆码", nums,
		// 2d));

		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630bf", "万位", nums, 1d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630bf", "千位", nums, 2d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630bf", "百位", nums, 3d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630bf", "十位", nums, 4d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630bf", "个位", nums, 5d));
		
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c0", "万位", nums, 1d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c0", "千位", nums, 2d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c0", "百位", nums, 3d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c0", "十位", nums, 4d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c0", "个位", nums, 5d));
		
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c1", "万位", nums, 1d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c1", "千位", nums, 2d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c1", "百位", nums, 3d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c1", "十位", nums, 4d));
		numLocateRepo.save(buildNumLocate("5c362f66be5a6b5eb60630c1", "个位", nums, 5d));
	}

}
