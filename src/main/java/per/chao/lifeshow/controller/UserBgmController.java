package per.chao.lifeshow.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.entity.pojo.Bgms;
import per.chao.lifeshow.service.IBgmService;
import per.chao.lifeshow.utils.QueryPageUtils;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/22 21:13
 **/
@RestController
@RequestMapping("/u/bgm")
public class UserBgmController {
	@Autowired
	private IBgmService bgmService;

	/**
	 * 根据关键词分页查询歌单列表（关键词可为空，则全部查询）
	 *
	 * @param pages
	 * @param keyword
	 * @return
	 */
	@ApiOperation("根据关键词分页查询歌单列表（关键词可为空，则全部查询）")
	@GetMapping("/songlist")
	public JsonResult getSongList(Integer pages, String keyword) {
		Page<Bgms> bgmsPage = QueryPageUtils.getPage(new Page<>(), pages, 20, true, "cited");
		LambdaQueryWrapper<Bgms> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Bgms::getStatus,1).like(StringUtils.isNotEmpty(keyword), Bgms::getBgmName, keyword).or(StringUtils.isNotEmpty(keyword), lqw -> {
			lqw.like(Bgms::getSinger, keyword);
		});
		return JsonResult.ok(bgmService.processDataWithCover(bgmService.page(bgmsPage, wrapper)));
	}

	/**
	 * 获取歌曲的在线试听链接
	 *
	 * @param mid
	 * @return
	 */
	@Deprecated
	@ApiOperation("获取歌曲的在线试听链接")
	@GetMapping("/src")
	public JsonResult getSongPlaySrc(String mid) {
		String songPlaySrc = bgmService.getSongPlaySrc(mid);
		if (StringUtils.isNotEmpty(songPlaySrc)) {
			return JsonResult.ok(songPlaySrc);
		}
		return JsonResult.error("当前歌曲不可播放");
	}
}
