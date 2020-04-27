package per.chao.lifeshow.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
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
 * @since 2020-03-01
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_admin_auths")
@FieldNameConstants
@ApiModel(value="AdminAuths对象", description="")
public class AdminAuths implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "管理员授权登录表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "管理员帐号")
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String account;

    @ApiModelProperty(value = "管理员密码")
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String password;

    @ApiModelProperty(value = "最近登录时间")
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Long lastLoginDate;

    @ApiModelProperty(value = "管理员信息id",example = "1")
    private Integer adminId;

    @ApiModelProperty(value = "密码是否需要修改（1：不需要；0：需要）")
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String pwdLastSet;


}
