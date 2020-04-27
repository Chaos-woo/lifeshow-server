package per.chao.lifeshow.entity.vo;

import lombok.Data;

import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/19 10:16
 **/
@Data
public class UserInfoVO {
	private String avatarUrl;
	private String city;
	private String country;
	private String gender;
	private String nickName;
	private String province;
	private Integer id;
	private Map<String,Object> external;
}
