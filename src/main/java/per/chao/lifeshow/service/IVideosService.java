package per.chao.lifeshow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import per.chao.lifeshow.entity.pojo.Videos;
import per.chao.lifeshow.entity.vo.UserVideoVO;
import per.chao.lifeshow.entity.vo.VideoManageVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/29 13:52
 **/
public interface IVideosService extends IService<Videos> {
	Integer uploadVideo(Videos videos, String activName, MultipartFile file);

	Boolean uploadCover(Integer videoId, MultipartFile file);

	Map<String,Object> selectByUserId(Integer id, Integer pages, Integer limits);

	Map<String, Object> selectCollectedByUserId(Integer id, Integer pages, Integer limits);

	Map<String, Object> selectByKeyword(String keyword, Integer pages, Integer limits);

	List<UserVideoVO> randVideoSet(Integer id,Boolean attention);

	List<UserVideoVO> randActivVideoSet(Integer id, Integer activId);

	UserVideoVO getOneVideoDetail(Integer id, Integer videoId);

	void handleLiked(Integer videoId,Integer id,Boolean isLiked);

	void handleCollected(Integer videoId, Integer id, Boolean isCollected);

	List<VideoManageVO> listVideoManageVO(Collection<Videos> videos,Boolean external);
}
