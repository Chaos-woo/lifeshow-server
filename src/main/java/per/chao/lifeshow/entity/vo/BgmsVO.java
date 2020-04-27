package per.chao.lifeshow.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/9 10:00
 **/
@Data
public class BgmsVO {
	@JsonProperty("songname")
	private String songName;
	@JsonProperty("singer")
	private List<Object> singer;
	@JsonProperty("songmid")
	private String songMid;
	private String albumid;
}
