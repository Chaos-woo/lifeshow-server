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
@TableName("tb_user_reports")
@FieldNameConstants
@ApiModel(value="UserReports对象", description="")
public class UserReports implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户举报表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "举报内容")
    private String reportContent;

    @ApiModelProperty(value = "举报时间")
    private Long createdAt;

    @ApiModelProperty(value = "举报用户")
    private Integer createdBy;

    @ApiModelProperty(value = "被举报短视频")
    private Integer videoId;

    @ApiModelProperty(value = "处理结果（0：未处理 1：已处理）")
    private String result;


}
