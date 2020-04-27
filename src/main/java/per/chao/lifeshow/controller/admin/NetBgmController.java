package per.chao.lifeshow.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.entity.pojo.Bgms;
import per.chao.lifeshow.service.IBgmService;

import javax.validation.Valid;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/9 13:30
 **/
@Api("背景音乐搜索/下载接口")
@Controller
@RequestMapping("/admin")
public class NetBgmController {
	@Autowired
	private IBgmService bgmService;

	private final String LIST_URL = "admin/bgm_download::bgmList";

	/**
	 * 背景音乐搜索/下载页面
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation("背景音乐搜索/下载页面")
	@GetMapping("/bgm/download")
	public String downloadPage(Model model) {
		model.addAttribute("page", bgmService.getRandSongList());
		return "admin/bgm_download";
	}

	/**
	 * 通过关键词搜索
	 *
	 * @param model
	 * @param pages
	 * @param keyword
	 * @return
	 */
	@ApiOperation("通过关键词搜索")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pages", value = "请求页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "keyword", value = "关键词（歌曲名或演唱者等关键信息）", required = false, dataType = "String")
	})
	@PostMapping("/bgm/d/key/search")
	public String search(Model model, Integer pages, String keyword) {
		model.addAttribute("page", bgmService.listByKeyword(pages, keyword));
		return LIST_URL;
	}

//	/**
//	 * 通过固定歌单类别选择
//	 *
//	 * @param model
//	 * @param category
//	 * @return
//	 */
//	@ApiOperation("通过固定歌单类别选择")
//	@ApiImplicitParam(name = "category", value = "歌单类别id", required = true, dataType = "Integer")
//	@PostMapping("/bgm/d/catg/search")
//	public String quickSearch(Model model, Integer category) {
//		if (category == -1) {
//			model.addAttribute("page", bgmService.getTopSongList());
//		} else {
//			model.addAttribute("page", bgmService.getRandSongList(category));
//		}
//		return LIST_URL;
//	}

	@ApiOperation("音乐下载")
	@ApiImplicitParam(name = "bgms", value = "Bgms实体类")
	@GetMapping("/bgm/d")
	@ResponseBody
	public JsonResult download(@Valid Bgms bgms) {
		Bgms b = bgmService.download(bgms);
		if (b == null) {
			return JsonResult.error("<" + bgms.getBgmName() + ">" + "下载失败");
		}
		return JsonResult.okNonData("<" + bgms.getBgmName() + ">" + "下载成功");
	}
}
