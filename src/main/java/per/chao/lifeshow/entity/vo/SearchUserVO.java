package per.chao.lifeshow.entity.vo;

import lombok.Data;

import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/27 15:27
 **/
@Data
public class SearchUserVO {
	private String nickName;
	private Integer id;
	private String avatarUrl;
	private String fans;
	private String works;
	private Map<String,String> external;
}
