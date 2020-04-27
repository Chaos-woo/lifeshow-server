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
@TableName("tb_user_blacklist")
@FieldNameConstants
@ApiModel(value="UserBlacklist对象", description="")
public class UserBlacklist implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户黑名单id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "某用户id")
    private Integer userId;

    @ApiModelProperty(value = "被列入黑名单的用户id",example = "1")
    private Integer blackUserId;


}
