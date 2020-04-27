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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_bgms")
@FieldNameConstants
@ApiModel(value="Bgms对象", description="")
public class Bgms implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "背景音乐信息表id",example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "bgm接口mid",required = true)
	@NotBlank(message = "mid不能为空")
    private String mid;

    @ApiModelProperty(value = "音乐名",required = true)
	@NotBlank(message = "name不能为空")
    private String bgmName;

    @ApiModelProperty(value = "演唱者")
    private String singer;

    @ApiModelProperty(value = "音频本地链接")
    private String bgmPath;

	// 本地
    @ApiModelProperty(value = "封面图")
    private String bgmCover;

    @ApiModelProperty(value = "音频长度（毫秒）")
    private Long bgmSeconds;

    @ApiModelProperty(value = "封面图mid")
    private String albumid;

    @ApiModelProperty(value = "音乐被使用次数")
	private Integer cited;

    @ApiModelProperty(value = "音乐用户是否可检索（0：不可；1：可）")
	private Integer status;
}
