package per.chao.lifeshow.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import per.chao.lifeshow.entity.pojo.Bgms;
import per.chao.lifeshow.service.IBgmService;
import per.chao.lifeshow.utils.QueryPageUtils;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/7 15:50
 **/
@Api("背景音乐本地操作接口")
@Controller
@RequestMapping("/admin")
public class BgmController {

	@Autowired
	private IBgmService bgmService;

	/**
	 * 本地音乐查看页面
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation("本地音乐查看页面")
	@GetMapping("/bgm")
	public String bgm(Model model) {
		Page<Bgms> bgmsPage = QueryPageUtils.getPage(new Page<>(), 1, 10, true, "id");
		Page<Bgms> page = bgmService.processDataWithCover(bgmService.page(bgmsPage, new QueryWrapper<>()));
		model.addAttribute("page", page);
		return "admin/bgm_manager";
	}

	/**
	 * 本地音乐关键词查询
	 *
	 * @param model
	 * @param pages
	 * @param bgm
	 * @return
	 */
	@ApiOperation("本地音乐关键词查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pages", value = "请求页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "bgm", value = "背景音乐名或演唱者", dataType = "String")
	})
	@PostMapping("/bgm/search")
	public String search(Model model, Integer pages, String bgm) {
		Page<Bgms> bgmsPage = QueryPageUtils.getPage(new Page<>(), pages, 10, true, "id");
		QueryWrapper<Bgms> wrapper = new QueryWrapper<>();
		wrapper.like(StringUtils.isNotEmpty(bgm), "bgm_name", bgm).or().like(StringUtils.isNotEmpty(bgm), "singer", bgm);
		Page<Bgms> page = bgmService.processDataWithCover(bgmService.page(bgmsPage, wrapper));
		model.addAttribute("page", page);
		return "admin/bgm_manager::bgmList";
	}

	/**
	 * 本地背景音乐删除
	 *
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@ApiOperation("本地背景音乐删除")
	@ApiImplicitParam(name = "id", value = "被删除音乐id", required = true, dataType = "Integer")
	@GetMapping("/bgm/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		if (bgmService.deleteBgmById(id)) {
			redirectAttributes.addFlashAttribute("message", "删除成功");
		} else {
			redirectAttributes.addFlashAttribute("message", "删除失败");
		}
		return "redirect:/admin/bgm";
	}

	/**
	 * 本地音乐截取
	 * @param start
	 * @param end
	 * @param id
	 * @return
	 */
	@ApiOperation("本地音乐截取")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "start",value = "开始时间",required = true),
			@ApiImplicitParam(name = "end",value = "结束时间",required = true),
	})
	@GetMapping("/bgm/edit")
	public String editLocalMusic(Integer start,Integer end,Integer id){
		bgmService.bgmCutting(start, end, id);
		return "redirect:/admin/bgm";
	}

}
