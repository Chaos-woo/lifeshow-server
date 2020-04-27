package per.chao.lifeshow.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/11 17:35
 **/
public class NetDownloadUtils {
	public static void download(String urlString, String filename) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 输入流
		InputStream is = con.getInputStream();
		String code = con.getHeaderField("Content-Encoding");
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		File file = new File(filename);
		if (!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		if ((null != code) && code.equals("gzip")) {
			GZIPInputStream gis = new GZIPInputStream(is);
			int len;
			OutputStream os = new FileOutputStream(filename);
			while ((len = gis.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			gis.close();
			os.close();
			is.close();
		} else {
			int len;
			OutputStream os = new FileOutputStream(filename);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			os.close();
			is.close();
		}
	}

}
