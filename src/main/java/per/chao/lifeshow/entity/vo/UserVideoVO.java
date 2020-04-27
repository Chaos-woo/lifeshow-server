package per.chao.lifeshow.entity.vo;

import lombok.Data;

import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/1 19:20
 **/
@Data
public class UserVideoVO {
	private Integer videoId;
	private String title;
	private String cover;
	private String description;
	private String videoSrc;
	private Integer authorId;
	private String author;
	private String authorAvatar;
	private String active;
	private String likedCount;
	private String commentCount;
	private String favoredCount;
	private String sharedCount;
	private Boolean isLiked;
	private Boolean isCollected;
	private Map<String,Object> external;
}
