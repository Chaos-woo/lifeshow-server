package per.chao.lifeshow.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
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
@TableName("tb_activs")
@FieldNameConstants
@ApiModel(value="Activs对象", description="")
public class Activs implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "热门活动表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "活动名",required = true)
	@NotBlank(message = "活动名不能为空")
    private String activName;

    @ApiModelProperty(value = "该活动下的视频数量",example = "1")
	@TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Integer activsCount;


}
