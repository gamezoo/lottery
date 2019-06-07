/**
 * 由于js存在精度丢失的问题,需要对其进行四舍五入处理
 * 
 * @param num
 * @param digit
 *            小数位数, 不填则默认4为小数
 * @returns
 */
function numberFormat(num, digit) {
	if (digit == null) {
		digit = 4;
	}
	return parseFloat(Number(num).toFixed(digit));
}

		
