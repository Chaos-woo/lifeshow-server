package per.chao.lifeshow.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/4 17:58
 **/
public class QueryPageUtils {
	public static <T> Page<T> getPage(Page<T> page, Integer current, Integer size, Boolean asc, String ascField) {
		page.setCurrent(current);
		page.setSize(size);
		if (asc != null && StringUtils.isNotEmpty(ascField)) {
			if (asc) {
				page.setAsc(ascField);
			} else {
				page.setDesc(ascField);
			}
		}
		return page;
	}
}
