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
@TableName("tb_video_stat")
@FieldNameConstants
@ApiModel(value="VideoStat对象", description="")
public class VideoStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "短视频数据表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "点赞数",example = "1")
    private Integer likedCount;

    @ApiModelProperty(value = "评论数",example = "1")
    private Integer commentCount;

    @ApiModelProperty(value = "收藏数",example = "1")
    private Integer favoredCount;

    @ApiModelProperty(value = "转发数",example = "1")
    private Integer sharedCount;

    @ApiModelProperty(value = "短视频信息id",example = "1")
    private Integer videoId;

	public VideoStat(Integer likedCount, Integer commentCount, Integer favoredCount, Integer sharedCount, Integer videoId) {
		this.likedCount = likedCount;
		this.commentCount = commentCount;
		this.favoredCount = favoredCount;
		this.sharedCount = sharedCount;
		this.videoId = videoId;
	}
}
