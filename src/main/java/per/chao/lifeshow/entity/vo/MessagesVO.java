package per.chao.lifeshow.entity.vo;

import lombok.Data;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/8 17:55
 **/
@Data
public class MessagesVO {
	private Integer id;
	private String title;
	private String content;
	private String createdAt;
	private String extraLink;
	private Integer typeId;
}
