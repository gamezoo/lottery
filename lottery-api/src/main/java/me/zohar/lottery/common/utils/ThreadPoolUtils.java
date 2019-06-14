package me.zohar.lottery.common.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {

	/**
	 * 核心线程数，会一直存活，即使没有任务，线程池也会维护线程的最少数量
	 */
	private static final int SIZE_CORE_POOL = 5;
	/**
	 * 线程池维护线程的最大数量
	 */
	private static final int SIZE_MAX_POOL = 10;
	/**
	 * 线程池维护线程所允许的空闲时间
	 */
	private static final long ALIVE_TIME = 2000;

	/**
	 * 同步开奖线程池
	 */
	private static ThreadPoolExecutor syncLotteryThreadPool = new ThreadPoolExecutor(SIZE_CORE_POOL, SIZE_MAX_POOL,
			ALIVE_TIME, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100),
			new ThreadPoolExecutor.CallerRunsPolicy());

	/**
	 * 开奖结算线程池
	 */
	private static ScheduledThreadPoolExecutor lotterySettlementPool = new ScheduledThreadPoolExecutor(4);

	/**
	 * 充值结算线程池
	 */
	private static ScheduledThreadPoolExecutor rechargeSettlementPool = new ScheduledThreadPoolExecutor(4);

	/**
	 * 登录日志线程池
	 */
	private static ScheduledThreadPoolExecutor loginLogPool = new ScheduledThreadPoolExecutor(2);

	/**
	 * 投注返点结算线程池
	 */
	private static ScheduledThreadPoolExecutor bettingRebateSettlementPool = new ScheduledThreadPoolExecutor(4);

	static {
		syncLotteryThreadPool.prestartAllCoreThreads();
	}

	public static ThreadPoolExecutor getSyncLotteryThreadPool() {
		return syncLotteryThreadPool;
	}

	public static ScheduledThreadPoolExecutor getLotterySettlementPool() {
		return lotterySettlementPool;
	}

	public static ScheduledThreadPoolExecutor getRechargeSettlementPool() {
		return rechargeSettlementPool;
	}

	public static ScheduledThreadPoolExecutor getLoginLogPool() {
		return loginLogPool;
	}

	public static ScheduledThreadPoolExecutor getBettingRebateSettlementPool() {
		return bettingRebateSettlementPool;
	}
}
