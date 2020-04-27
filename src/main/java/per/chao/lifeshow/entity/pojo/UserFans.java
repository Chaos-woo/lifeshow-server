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
@TableName("tb_user_fans")
@FieldNameConstants
@ApiModel(value="UserFans对象", description="")
public class UserFans implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户粉丝表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "粉丝状态（0：只是粉丝 1：互相关注）")
    private String status;

    @ApiModelProperty(value = "关注时间")
    private Long followedDate;

    @ApiModelProperty(value = "被关注用户id",example = "1")
    private Integer userId;

    @ApiModelProperty(value = "粉丝用户id",example = "1")
    private Integer fansId;


}
