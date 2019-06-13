package me.zohar.lottery.issue.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SyncLotteryNumMsg {

	private String gameCode;

	private Long issueNum;

	private Integer retries;

}
