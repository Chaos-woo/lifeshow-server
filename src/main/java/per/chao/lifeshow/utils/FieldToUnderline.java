package per.chao.lifeshow.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/27 14:55
 **/
public class FieldToUnderline {
	public static String to(Object o){
		return StringUtils.camelToUnderline(o.toString());
	}
}
