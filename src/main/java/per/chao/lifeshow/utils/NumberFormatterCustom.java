package per.chao.lifeshow.utils;

/**
 * Description: 用于将大数字转为科学计数法表示的工具类
 *
 * @author W.Chao
 * @date 2020/3/27 16:40
 **/
public class NumberFormatterCustom {
	private static final Integer ZERO_LIMITS = 100;
	private static final Integer ONE_LIMITS = 1000;
	private static final Integer TWO_LIMITS = 10000;

	public static String to(Integer num) {
		StringBuilder format;
		if (num <= ONE_LIMITS) {
			return String.valueOf(num);
		} else if (num <= TWO_LIMITS) {
			format = new StringBuilder();
			Integer integer = num / ONE_LIMITS;
			Integer decimal = num % ONE_LIMITS;
			String tmp = "";
			if (decimal < ZERO_LIMITS) {
				tmp = "0" + decimal;
			} else {
				tmp = String.valueOf(decimal);
			}
			return format.append(integer).append(".").append(tmp, 0, 2).append("k").toString();
		} else {
			format = new StringBuilder();
			Integer integer = num / TWO_LIMITS;
			Integer decimal = num % TWO_LIMITS;
			String tmp = "";
			if (decimal > ZERO_LIMITS && decimal < ONE_LIMITS) {
				tmp = "0" + decimal;
			} else if (decimal < ZERO_LIMITS){
				tmp = "00" + decimal;
			}else {
				tmp = String.valueOf(decimal);
			}
			return format.append(integer).append(".").append(tmp, 0, 2).append("w").toString();
		}
	}
}
