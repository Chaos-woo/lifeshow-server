package per.chao.lifeshow.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.entity.pojo.Activs;
import per.chao.lifeshow.service.IActivService;
import per.chao.lifeshow.utils.QueryPageUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/27 11:24
 **/
@Api("用户热门活动接口")
@RestController
@RequestMapping("/u/activ")
public class UserActivController {
	@Autowired
	@Qualifier("activServiceImpl")
	private IActivService activService;

	/**
	 * 分页查询热门活动
	 *
	 * @param page
	 * @return
	 */
	@ApiOperation("分页查询热门活动")
	@GetMapping
	public JsonResult listActiv(Integer page) {
		Page<Activs> activsPage = QueryPageUtils.getPage(new Page<>(), page, 30, true, "id");
		Page<Activs> activsList = activService.page(activsPage, new QueryWrapper<>());
		if (activsList != null) {
			return JsonResult.ok(activsList);
		}
		return JsonResult.error("服务期出现了一点小问题");
	}

	/**
	 * 通过关键字查询
	 *
	 * @param keyword
	 * @return
	 */
	@ApiOperation("通过关键字查询")
	@GetMapping("/key")
	public JsonResult search(String keyword) {
		Page<Activs> activsPage = QueryPageUtils.getPage(new Page<>(), 1, 1000, true, "id");
		// 简单的模糊匹配
		QueryWrapper<Activs> wrapper = new QueryWrapper<>();
		wrapper.like(StringUtils.isNotEmpty(keyword), "activ_name", keyword);
		return JsonResult.ok(activService.page(activsPage, wrapper));
	}

	/**
	 * 获取所有活动的名字
	 *
	 * @return
	 */
	@ApiOperation("获取所有活动的名字")
	@GetMapping("/all")
	public JsonResult listAllActivName() {
		List<Activs> activsList = activService.list();
		if (activsList != null) {
			List<String> names = new LinkedList<>();
			activsList.forEach(a -> names.add(a.getActivName()));
			return JsonResult.ok(names);
		}
		return JsonResult.error("服务期出现了一点小问题");
	}
}
