package per.chao.lifeshow.entity.vo;

import lombok.Data;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/3 13:50
 **/
@Data
public class UserCommentVO {
	private Integer videoId;
	private String authorAvatar;
	private Integer authorId;
	private String author;
	private String content;
	private String videoCover;
}
