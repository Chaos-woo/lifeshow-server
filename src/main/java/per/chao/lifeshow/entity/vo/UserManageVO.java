package per.chao.lifeshow.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/4 15:00
 **/
@Data
@AllArgsConstructor
public class UserManageVO {
	private String openid;
	private Integer id;
	private String avatar;
	private String nickname;
	private String gender;
	private Long regDate;
	private Long lastLoginDate;
	private String status;
	private String stat;
	private String city;
	private String country;
	private String province;
}
