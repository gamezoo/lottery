package me.zohar.lottery.common.param;

import lombok.Data;

/**
 * 分页条件入参
 * 
 * @author zohar
 * @date 2019年1月16日
 *
 */
@Data
public class PageParam {

	/**
	 * 页码
	 */
	private Integer pageNum;

	/**
	 * 每页大小
	 */
	private Integer pageSize;

	/**
	 * 排序字段
	 */
	private String propertie;

	/**
	 * 排序方式
	 */
	private String direction;

}
