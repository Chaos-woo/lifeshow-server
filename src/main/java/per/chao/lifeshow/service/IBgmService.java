package per.chao.lifeshow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import per.chao.lifeshow.entity.pojo.Bgms;

import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/7 16:50
 **/
public interface IBgmService extends IService<Bgms> {
	Map<String, Object> listByKeyword(Integer pages, String keyword);

//	Map<String, Object> getTopSongList();

//	Map<String, Object> getRandSongList(Integer id);

	Map<String, Object> getRandSongList();

	Bgms download(Bgms bgms);

	Boolean deleteBgmById(Integer id);

	Page<Bgms> processDataWithCover(Page<Bgms> page);

	String getSongPlaySrc(String mid);

	void bgmCutting(Integer start, Integer end, Integer id);
}
