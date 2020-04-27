package per.chao.lifeshow.utils;

import per.chao.lifeshow.utils.m4autildependency.M4AInfo;

import java.io.*;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/5 10:43
 **/
public class M4aUtils {
	public static Long getDuration(String path){
		File f = new File(path);
		long duration = 0L;
		try {
			InputStream is = new FileInputStream(f);
			M4AInfo info = new M4AInfo(is);
			duration = info.getDuration();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return duration;
	}
}
