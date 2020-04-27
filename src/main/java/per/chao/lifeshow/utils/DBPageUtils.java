package per.chao.lifeshow.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/6 10:15
 **/
public class DBPageUtils {
	public static <T,M> Map<String,Object> getPage(Page<T> page, List<M> list){
		Map<String,Object> map = new HashMap<>();
		map.put("records",list);
		map.put("total",page.getTotal());
		map.put("size",page.getSize());
		map.put("current",page.getCurrent());
		map.put("orders",page.getOrders());
		map.put("pages",page.getPages());
		return map;
	}

	public static <M> Map<String,Object> toPage(List<M> list,Integer pages,Integer total,Integer size,Integer current){
		Map<String,Object> map = new HashMap<>();
		map.put("records",list);
		map.put("total",total);
		map.put("size",size);
		map.put("current",current);
		map.put("pages",pages);
		return map;
	}
}
