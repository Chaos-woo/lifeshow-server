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
@TableName("tb_message_type")
@FieldNameConstants
@ApiModel(value="MessageType对象", description="")
public class MessageType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息类型表id",example = "1")
		@TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @ApiModelProperty(value = "类型名")
    private String typeName;


}
