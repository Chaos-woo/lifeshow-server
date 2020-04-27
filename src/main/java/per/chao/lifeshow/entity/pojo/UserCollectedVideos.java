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
@TableName("tb_user_collected_videos")
@FieldNameConstants
@ApiModel(value="UserCollectedVideos对象", description="")
public class UserCollectedVideos implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户收藏短视频表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "点赞用户id",example = "1")
    private Integer userId;

    @ApiModelProperty(value = "被收藏的短视频id",example = "1")
    private Integer videoId;


}
