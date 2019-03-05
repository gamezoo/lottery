package me.zohar.lottery.rechargewithdraw.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * muspay回调参数
 * 
 * @author zohar
 * @date 2019年1月21日
 *
 */
@Data
public class MuspayCallbackParam {

	/**
	 * 商户ID
	 */
	@NotBlank(message = "fxid不能为空")
	private String fxid;

	/**
	 * 商户订单号
	 */
	@NotBlank(message = "fxddh不能为空")
	private String fxddh;

	/**
	 * 商品名称
	 */
	private String fxdesc;

	/**
	 * 平台订单号
	 */
	@NotBlank(message = "fxorder不能为空")
	private String fxorder;

	/**
	 * 支付金额
	 */
	@NotNull(message = "fxfee不能为空")
	private String fxfee;

	/**
	 * 附加信息
	 */
	private String fxattch;

	/**
	 * 支付成功时的时间,unix时间戳
	 */
	@NotNull(message = "fxtime不能为空")
	private Long fxtime;

	/**
	 * 订单状态,1代表支付成功
	 */
	@NotNull(message = "fxstatus不能为空")
	private String fxstatus;

	/**
	 * 通过签名算法计算得出的签名值
	 */
	@NotBlank(message = "fxsign不能为空")
	private String fxsign;

}
