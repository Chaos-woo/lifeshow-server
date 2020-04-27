package per.chao.lifeshow.tools;

import per.chao.lifeshow.utils.MD5Utils;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/3 11:36
 **/
public class MD5Test {
	public static void main(String[] args) {
		String md5 = MD5Utils.getMd5("admin");
		System.out.println("普通md5 : " + md5);
		System.out.println("加盐md5 : " + MD5Utils.getSaltMd5("admin"));

		System.out.println("验证普通md5 : " + MD5Utils.verifyMd5("admin",MD5Utils.getMd5("admin")));
		System.out.println("验证加盐md5 : " + MD5Utils.verifySaltMd5("admin",MD5Utils.getSaltMd5("admin")));


		System.out.println(MD5Utils.getSaltMd5(md5));
	}
}
