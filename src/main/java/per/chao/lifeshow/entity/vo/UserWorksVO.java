package per.chao.lifeshow.entity.vo;

import lombok.Data;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/1 9:03
 **/
@Data
public class UserWorksVO {
	private Integer id;
	private String title;
	private String cover;
	private String likedCount;
	private String commentCount;
	private String sharedCount;
	private String favoredCount;
	private String danmakuCount;
	private String createdBy;
	private String createdAt;
}
