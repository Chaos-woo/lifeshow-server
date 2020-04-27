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
@TableName("tb_admin_operate_log")
@FieldNameConstants
@ApiModel(value="AdminOperateLog对象", description="")
public class AdminOperateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "管理员操作日志表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作内容")
    private String logContent;

    @ApiModelProperty(value = "操作时间")
    private Long createdAt;

    @ApiModelProperty(value = "操作人")
    private String createdBy;

    @ApiModelProperty(value = "操作类型",example = "1")
    private Integer logType;


}
