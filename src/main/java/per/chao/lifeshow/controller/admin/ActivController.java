package per.chao.lifeshow.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.entity.pojo.Activs;
import per.chao.lifeshow.service.IActivService;
import per.chao.lifeshow.utils.FieldToUnderline;
import per.chao.lifeshow.utils.QueryPageUtils;

import javax.validation.Valid;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/4 11:55
 **/
@Api("热门活动操作接口")
@Controller
@RequestMapping("/admin")
public class ActivController {
	@Autowired
	@Qualifier("activServiceImpl")
	private IActivService activService;

	public static final String REDIRECT_ACTIV_URL = "redirect:/admin/activs/0";
	public static final String ACTIV_URL = "admin/activ_manager";

	/**
	 * 热门活动页面
	 *
	 * @param model
	 * @param status
	 * @return
	 */
	@ApiOperation("热门活动页面")
	@ApiImplicitParam(name = "status", value = "页面状态码（提示信息修改0/1）", required = true, dataType = "Integer")
	@GetMapping("/activs/{status}")
	public String activ(Model model, @PathVariable("status") Integer status) {
		Page<Activs> activsPage = QueryPageUtils.getPage(new Page<>(), 1, 10, true, "id");
		Page<Activs> page = activService.page(activsPage, new QueryWrapper<>());
		if (status == 1) {
			model.addAttribute("message", "活动修改成功");
		}
		model.addAttribute("page", page);
		return ACTIV_URL;
	}

	/**
	 * 热门活动搜索
	 *
	 * @param model
	 * @param pages
	 * @param activ
	 * @return
	 */
	@ApiOperation("热门活动搜索")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pages", value = "请求页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "activ", value = "关键词", dataType = "String")
	})
	@PostMapping("/activs/search")
	public String search(Model model, Integer pages, String activ) {
		Page<Activs> activsPage = QueryPageUtils.getPage(new Page<>(), pages, 10, true, "id");
		// 简单的模糊匹配
		QueryWrapper<Activs> wrapper = new QueryWrapper<>();
		wrapper.like(StringUtils.isNotEmpty(activ), FieldToUnderline.to(Activs.Fields.activName), activ);
		model.addAttribute("page", activService.page(activsPage, wrapper));
		return "admin/activ_manager::activList";
	}

	/**
	 * 新增热门活动
	 *
	 * @param activ
	 * @param redirectAttributes
	 * @return
	 */
	@ApiOperation("新增热门活动")
	@ApiImplicitParam(name = "activ", value = "Activs实体类")
	@PostMapping("/activs/add")
	public String add(@Valid Activs activ, RedirectAttributes redirectAttributes) {
		Activs activ1 = activService.selectByName(activ.getActivName());
		if (activ1 != null) {
			redirectAttributes.addFlashAttribute("message", "活动已经存在");
		} else {
			if (activService.save(activ)) {
				redirectAttributes.addFlashAttribute("message", "活动添加成功");
			} else {
				redirectAttributes.addFlashAttribute("message", "活动添加失败");
			}
		}
		return REDIRECT_ACTIV_URL;
	}

	/**
	 * 热门活动信息获取
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("热门活动信息获取")
	@ApiImplicitParam(name = "id", value = "热门活动id", required = true, dataType = "Integer")
	@GetMapping("/activs/edit")
	@ResponseBody
	public JsonResult getActivInfo(@RequestParam Integer id) {
		Activs activ = activService.getById(id);
		return JsonResult.ok(activ);
	}

	/**
	 * 热门活动信息更新
	 *
	 * @param activ
	 * @return
	 */
	@ApiOperation("热门活动信息更新")
	@ApiImplicitParam(name = "activ", value = "Activs实体类")
	@PostMapping("/activs/edit")
	@ResponseBody
	public JsonResult editActiv(@Valid Activs activ) {
		Activs activ1 = activService.selectByName(activ.getActivName());
		if (activ1 != null) {
			return JsonResult.error("活动已经存在");
		}
		if (!activService.updateById(activ)) {
			return JsonResult.error("活动修改失败");
		}
		return JsonResult.okNonData();
	}

	/**
	 * 热门活动删除
	 *
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@ApiOperation("热门活动删除")
	@ApiImplicitParam(name = "id", value = "被删除活动id", required = true, dataType = "Integer")
	@GetMapping("/activs/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		activService.removeById(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return REDIRECT_ACTIV_URL;
	}
}
