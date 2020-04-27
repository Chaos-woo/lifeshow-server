package per.chao.lifeshow.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author W.Chao
 * @since 2020-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user_auths")
@FieldNameConstants
@ApiModel(value="UserAuths对象", description="")
public class UserAuths implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户授权登录表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String openid;

    @ApiModelProperty(value = "微信token（密码）")
    private String credential;

    @ApiModelProperty(value = "登录状态，1：已登录 0：未登录")
    private String status;

    @ApiModelProperty(value = "上次离开时间")
    private Long lastLoginDate;

    @ApiModelProperty(value = "用户基本信息id",example = "1")
    private Integer userId;


}
