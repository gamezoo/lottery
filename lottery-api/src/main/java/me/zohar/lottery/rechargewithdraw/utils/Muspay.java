package me.zohar.lottery.rechargewithdraw.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.dictconfig.ConfigHolder;

public class Muspay {

	public static final String 支付成功状态 = "1";

	public static final String 发起支付成功状态 = "1";
	
	/**
	 * 生成回调的签名
	 * 
	 * @param fxstatus
	 *            订单状态
	 * @param fxddh
	 *            订单号
	 * @param fxfee
	 *            支付金额
	 * @return
	 */
	public static String generateCallbackSign(String fxstatus, String fxddh, String fxfee) {
		String fxid = ConfigHolder.getConfigValue("muspay.fxid");
		String secret = ConfigHolder.getConfigValue("muspay.secret");
		String signature = DigestUtil.md5Hex(fxstatus + fxid + fxddh + fxfee + secret);
		return signature;
	}

	/**
	 * 生成请求的签名
	 * 
	 * @param fxddh
	 *            订单号
	 * @param fxfee
	 *            支付金额
	 * @return
	 */
	public static String generateRequestSign(String fxddh, Double fxfee) {
		String fxid = ConfigHolder.getConfigValue("muspay.fxid");
		String fxnotifyurl = ConfigHolder.getConfigValue("muspay.asynNoticeUrl");
		String secret = ConfigHolder.getConfigValue("muspay.secret");
		String signature = DigestUtil.md5Hex(fxid + fxddh + fxfee + fxnotifyurl + secret);
		return signature;
	}

	/**
	 * 调用发起支付接口
	 * 
	 * @param fxddh
	 * @param fxfee
	 * @param fxpay
	 * @return 返回扫码支付页面地址
	 */
	public static String sendRequest(String fxddh, Double fxfee, String fxpay) {
		Map<String, Object> params = new HashMap<>();
		params.put("fxid", ConfigHolder.getConfigValue("muspay.fxid"));
		params.put("fxddh", fxddh);
		params.put("fxdesc", "订单" + fxfee);
		params.put("fxfee", fxfee);
		params.put("fxnotifyurl", ConfigHolder.getConfigValue("muspay.asynNoticeUrl"));
		params.put("fxbackurl", ConfigHolder.getConfigValue("muspay.ssynNoticeUrl"));
		params.put("fxpay", fxpay);
		params.put("fxnotifystyle", "2");
		params.put("fxsmstyle", "1");
		params.put("fxsign", generateRequestSign(fxddh, fxfee));
		params.put("fxip", "192.168.1.1");
		String result = HttpUtil.post(ConfigHolder.getConfigValue("muspay.payUrl"), params);
		System.err.println(result);
		if (StrUtil.isEmpty(result)) {
			throw new BizException(BizError.发起支付异常);
		}
		JSONObject resultJsonObject = JSON.parseObject(result);
		if (!发起支付成功状态.equals(resultJsonObject.getString("status"))) {
			throw new BizException(BizError.发起支付异常);
		}
		return resultJsonObject.getString("payurl");
	}

}
