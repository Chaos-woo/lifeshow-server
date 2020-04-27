package per.chao.lifeshow.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/1/13 14:41
 **/
public class JsonUtils {
	public static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 将对象转换为json
	 *
	 * @param data
	 * @return
	 */
	public static String object2oJson(Object data) {
		try {
			return MAPPER.writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将json转换为指定类型pojo对象
	 *
	 * @param jsonData
	 * @param beanType
	 * @param <T>
	 * @return
	 */
	public static <T> T json2Pojo(String jsonData, Class<T> beanType) {
		try {
			return MAPPER.readValue(jsonData, beanType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将json转换为指定类型的pojo对象的list
	 *
	 * @param jsonData
	 * @param beanType
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> json2PojoWithList(String jsonData, Class<T> beanType) {
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
		try {
			return MAPPER.readValue(jsonData, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
