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
@TableName("tb_user_stat")
@FieldNameConstants
@ApiModel(value="UserStat对象", description="")
public class UserStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户数据表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关注数",example = "1")
    private Integer followersCount;

    @ApiModelProperty(value = "粉丝数",example = "1")
    private Integer fansCount;

    @ApiModelProperty(value = "作品数",example = "1")
    private Integer worksCount;

    @ApiModelProperty(value = "点赞数",example = "1")
    private Long receivedLikedCount;

    @ApiModelProperty(value = "用户基本信息id",example = "1")
    private Integer userId;


}
