package per.chao.lifeshow.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/9 11:10
 **/
public class NetBgmPageUtils {
	public static <T> Map<String, Object> getPage(Integer total, Integer size, Integer current, Integer pages, List<T> records) {
		Map<String, Object> map = new HashMap<>();
		map.put("total", total);
		map.put("size", size);
		map.put("current", current);
		map.put("pages", pages);
		map.put("records", records);
		return map;
	}
}
