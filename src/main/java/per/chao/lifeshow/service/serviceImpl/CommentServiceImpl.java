package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.Comments;
import per.chao.lifeshow.entity.vo.UserCommentVO;
import per.chao.lifeshow.mapper.CommentsMapper;
import per.chao.lifeshow.service.ICommentsService;
import per.chao.lifeshow.utils.DBPageUtils;
import per.chao.lifeshow.utils.FieldToUnderline;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/3 14:04
 **/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {
	@Autowired
	private CommentsMapper commentsMapper;

	@Value("${server.url}")
	private String serverUrl;

	public Map<String, Object> getVideoComments(Integer id, Integer pages, Integer limits) {
		List<UserCommentVO> commentsVOS = commentsMapper.selectCommentByVideoId(id, (pages - 1) * limits, limits);
		commentsVOS.forEach(c -> {
			File coverFile = new File(c.getVideoCover());
			c.setVideoCover(serverUrl + "/video/" + coverFile.getName());
		});
		return processComments(id, pages, limits, commentsVOS);
	}

	private Map<String, Object> processComments(Integer id, Integer pages, Integer limits, List<UserCommentVO> commentsVOS) {
		QueryWrapper<Comments> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(Comments.Fields.videoId), id);
		Integer total = commentsMapper.selectCount(wrapper);
		return DBPageUtils.toPage(commentsVOS, (total + limits - 1) / limits + 1, total, limits, pages);
	}

	public Map<String, Object> getUserComments(Integer id, Integer pages, Integer limits) {
		List<UserCommentVO> commentsVOS = commentsMapper.selectCommentByUserId(id, (pages - 1) * limits, limits);
		commentsVOS.forEach(c -> {
			File coverFile = new File(c.getVideoCover());
			c.setVideoCover(serverUrl + "/video/" + coverFile.getName());
		});
		return processComments(id, pages, limits, commentsVOS);
	}
}
