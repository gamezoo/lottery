package me.zohar.lottery.common.vo;

import java.util.List;

import lombok.Data;

@Data
public class PageResult<T> {

	/**
	 * 页码
	 */
	private int pageNum;

	/**
	 * 每页大小
	 */
	private int pageSize;

	/**
	 * 总页数
	 */
	private long totalPage;

	/**
	 * 总记录数
	 */
	private long total;

	/**
	 * 实际记录数
	 */
	private int size;

	/**
	 * 数据集
	 */
	private List<T> content;

	public PageResult(List<T> content, int pageNum, int pageSize, long total) {
		this.content = content;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalPage = total == 0 ? 0 : total % pageSize == 0 ? (total / pageSize) : (total / pageSize + 1);
		this.total = total;
		this.size = content.size();
	}

}
