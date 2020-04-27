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
 * @since 2020-02-28
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_admin_info")
@FieldNameConstants
@ApiModel(value="AdminInfo对象", description="")
public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "管理员信息表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "管理员昵称，和帐号相同")
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String nickname;

    @ApiModelProperty(value = "系统权限id",example = "1")
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Integer sysAuthId;


}
