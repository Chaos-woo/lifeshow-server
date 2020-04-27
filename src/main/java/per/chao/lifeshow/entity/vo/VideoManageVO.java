package per.chao.lifeshow.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/4 17:45
 **/
@Data
@AllArgsConstructor
public class VideoManageVO {
	private Integer id;
	private String title;
	private String cover;
	private String videoSrc;
	private String activ;
	private String description;
	private Long uploadDate;
	private Integer bgmId;
	private Double size;
	private String author;
	private Integer status;
	private String stat;
	private Map<String,Object> external;
}
