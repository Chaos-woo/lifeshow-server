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
@TableName("tb_messages")
@FieldNameConstants
@ApiModel(value="Messages对象", description="")
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户消息表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息创建时间")
    private Long createdAt;

    @ApiModelProperty(value = "消息创建者")
    private String createdBy;

    @ApiModelProperty(value = "消息附带链接")
    private String extraLink;

    @ApiModelProperty(value = "消息状态（是否被阅读，0：已阅读 1：未阅读）")
    private String status;

    @ApiModelProperty(value = "关联用户",example = "1")
    private Integer userId;

    @ApiModelProperty(value = "消息类型",example = "1")
    private Integer typeId;
}
