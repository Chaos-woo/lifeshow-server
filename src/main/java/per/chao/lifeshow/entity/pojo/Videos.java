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
@TableName("tb_videos")
@FieldNameConstants
@ApiModel(value="Videos对象", description="")
public class Videos implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "短视频信息表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "标题")
    private String videoTitle;

    @ApiModelProperty(value = "封面图路径")
    private String coverPath;

    @ApiModelProperty(value = "描述文字")
    private String videoDesc;

    @ApiModelProperty(value = "上传时间")
    private Long createdAt;

    @ApiModelProperty(value = "视频链接")
    private String videoPath;

    @ApiModelProperty(value = "短视频长度")
    private Long videoSeconds;

    @ApiModelProperty(value = "短视频宽度")
    private Double videoWidth;

    @ApiModelProperty(value = "短视频高度")
    private Double videoHeight;

    @ApiModelProperty(value = "短视频大小（KB）")
    private Double videoSize;

    @ApiModelProperty(value = "上传用户id",example = "1")
    private Integer createdBy;

    @ApiModelProperty(value = "短视频状态id",example = "1")
    private Integer statusId;

    @ApiModelProperty(value = "热门活动id",example = "1")
    private Integer activId;

    @ApiModelProperty(value = "背景音乐id",example = "1")
    private Integer bgmId;
}
