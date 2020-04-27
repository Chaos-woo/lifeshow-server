package per.chao.lifeshow.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/1 15:09
 **/
@Data
@AllArgsConstructor
public class AdminInfoVO {
	private Integer adminId;
	@ApiModelProperty(value = "管理员帐户名", required = true)
	@NotBlank(message = "帐户名不能为空")
	private String account;
	private String sessionKey;
	private Integer auth;
	private String authName;
	private Boolean pwdLastSet;
	private Long lastLoginDate;
	@ApiModelProperty(value = "管理员登录口令", required = true)
	@NotBlank(message = "密码不能为空")
	private String password;
}
