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
@TableName("tb_danmaku")
@FieldNameConstants
@ApiModel(value="Danmaku对象", description="")
public class Danmaku implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "短视频弹幕表",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "弹幕内容")
    private String content;

    @ApiModelProperty(value = "评论用户id",example = "1")
    private Integer createdBy;

    @ApiModelProperty(value = "被评论短视频id",example = "1")
    private Integer videoId;


}
