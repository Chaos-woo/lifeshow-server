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
@TableName("tb_stat_info")
@FieldNameConstants
@ApiModel(value="StatInfo对象", description="")
public class StatInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "统计数据总表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "数值")
    private Double value;

    @ApiModelProperty(value = "更新时间")
    private Long updateDate;

    @ApiModelProperty(value = "统计数据类型id",example = "1")
    private Integer typeId;


}
