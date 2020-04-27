package per.chao.lifeshow.entity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/16 19:34
 **/
@Data
public class WXSessionModel {
	private String openid;
	@JsonProperty("session_key")
	private String sessionKey;
	private Integer errcode;
	private String errmsg;
}
