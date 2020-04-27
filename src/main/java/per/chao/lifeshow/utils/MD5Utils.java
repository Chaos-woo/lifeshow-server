package per.chao.lifeshow.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/1/9 23:54
 **/
public class MD5Utils {
	private static final String CHARSET = "UTF-8";

	/**
	 * 普通md5
	 *
	 * @param content
	 * @return
	 */
	public static String getMd5(String content) {
		// 获取MD5实例
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return "";
		}

		// 将加密字符串转换为字符数组
		char[] charArray = content.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		// 开始加密
		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] digest = md5.digest(byteArray);
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			int var = b & 0xff;
			if (var < 16)
				sb.append("0");
			sb.append(Integer.toHexString(var));
		}
		return sb.toString();
	}

	/**
	 * 加盐加密md5算法
	 *
	 * @param content
	 * @return
	 */
	public static String getSaltMd5(String content) {
		// 生成一个16位的随机数
		Random random = new Random();
		StringBuilder sBuilder = new StringBuilder(16);
		sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
		int len = sBuilder.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
				sBuilder.append("0");
			}
		}
		// 生成最终的加密盐
		String salt = sBuilder.toString();
		content = md5Hex(content + salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = content.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = content.charAt(i / 3 * 2 + 1);
		}
		return String.valueOf(cs);
	}

	/**
	 * 使用Apache的Hex类实现Hex(16进制字符串和)和字节数组的互转
	 *
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String md5Hex(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(str.getBytes());
			return new String(new Hex().encode(digest));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return "";
		}
	}

	/**
	 * 验证传入内容经过md5加密后是否与某个md5串相同
	 *
	 * @param notVerifiedContent 未加密的串
	 * @param md5Str 已经加密的md5串
	 * @return
	 */
	public static boolean verifyMd5(String notVerifiedContent, String md5Str) {
		String content2Md5 = getMd5(notVerifiedContent);
		return StringUtils.equals(content2Md5, md5Str);
	}

	/**
	 * 验证加盐后是否和原文一致
	 *
	 * @param content 未加盐加密的串
	 * @param md5str 已经加盐加密的md5串
	 * @return
	 */
	public static boolean verifySaltMd5(String content, String md5str) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		for (int i = 0; i < 48; i += 3) {
			cs1[i / 3 * 2] = md5str.charAt(i);
			cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
			cs2[i / 3] = md5str.charAt(i + 1);
		}
		// 通过上述步骤得到salt
		String salt = new String(cs2);
		// 通过salt和未加密串得到md5串（equals参数1）
		return StringUtils.equals(md5Hex(content + salt), String.valueOf(cs1));
	}
}
