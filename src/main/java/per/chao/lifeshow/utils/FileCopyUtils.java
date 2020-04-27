package per.chao.lifeshow.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/19 12:58
 **/
public class FileCopyUtils {
	public static void copy(String srcPath, String destPath) throws IOException {
		// 打开输入流
		FileInputStream fis = new FileInputStream(srcPath);
		// 打开输出流
		FileOutputStream fos = new FileOutputStream(destPath);
		// 读取和写入信息
		int len = 0;
		while ((len = fis.read()) != -1) {
			fos.write(len);
		}
		// 关闭流  先开后关  后开先关
		fos.close(); // 后开先关
		fis.close(); // 先开后关
	}
}
