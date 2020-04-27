package per.chao.lifeshow.utils;

import java.util.Random;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/1 21:26
 **/
public class RandomNumberUtils {
	private static Integer INT_MAX = Integer.MAX_VALUE;
	private static Integer INT_MIN = Integer.MIN_VALUE;
	/**
	 * 生成指定区间随机数 (min, max)
	 *
	 * @param min 区间最小值(不包含)
	 * @param max 区间最大值(不包含)
	 * @return
	 */
	public static int getNum(int min, int max) {
		if (min >= max - 1) {
			return INT_MIN;
		}
		Random random = new Random();
		return random.nextInt(max - min - 1) + min + 1;
	}

	/**
	 * 生成指定区间随机数 [min, max)
	 *
	 * @param min 区间最小值(包含)
	 * @param max 区间最大值(不包含)
	 * @return
	 */
	public static int getNumIncludeMin(int min, int max) {
		if (min >= max) {
			return INT_MIN;
		}
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}

	/**
	 * 生成指定区间随机数 (min, max]
	 *
	 * @param min 区间最小值(不包含)
	 * @param max 区间最大值(包含)
	 * @return
	 */
	public static int getNumIncludeMax(int min, int max) {
		return getNumIncludeMin(min, max) + 1;
	}

	/**
	 * 生成指定区间随机数 [min, max]
	 *
	 * @param min 区间最小值(包含)
	 * @param max 区间最大值(包含)
	 * @return
	 */
	public static int getNumIncludeMinAndMax(int min, int max) {
		if (min >= max + 1) {
			return INT_MIN;
		}
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	/**
	 * 生成指定长度随机数
	 *
	 * @param len 指定长度
	 * @return
	 */
	public static int getNumByLen(int len) {
		if (len < 1 || len > 9) {
			return INT_MIN;
		}
		return Integer.parseInt(getNumStrByLen(len));
	}

	/**
	 * 生成指定长度随机数
	 *
	 * @param len 指定长度
	 * @return
	 */
	public static String getNumStrByLen(int len) {
		if (len < 1) {
			return INT_MIN.toString();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			if (i == 0) {
				sb.append(getNumIncludeMax(0, 9));
			} else {
				sb.append(getNumIncludeMinAndMax(0, 9));
			}
		}
		return sb.toString();
	}
}
