package per.chao.lifeshow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import per.chao.lifeshow.entity.pojo.Comments;

import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/3 14:04
 **/
public interface ICommentsService extends IService<Comments> {
	Map<String, Object> getVideoComments(Integer id, Integer pages, Integer limits);

	Map<String, Object> getUserComments(Integer id, Integer pages, Integer limits);
}
