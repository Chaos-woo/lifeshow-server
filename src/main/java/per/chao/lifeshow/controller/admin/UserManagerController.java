package per.chao.lifeshow.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import per.chao.lifeshow.entity.pojo.UserInfo;
import per.chao.lifeshow.entity.vo.UserManageVO;
import per.chao.lifeshow.service.IUserInfoService;
import per.chao.lifeshow.service.IUserService;
import per.chao.lifeshow.utils.DBPageUtils;
import per.chao.lifeshow.utils.FieldToUnderline;
import per.chao.lifeshow.utils.QueryPageUtils;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/4 14:40
 **/
@Api("用户管理操作接口")
@Controller
@RequestMapping("/admin")
public class UserManagerController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserInfoService userInfoService;

	/**
	 * 用户管理页
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation("用户管理页")
	@GetMapping("/user")
	public String user(Model model) {
		model.addAttribute("page", listUserInfoVO(1, new QueryWrapper<>()));
		return "admin/user_manager";
	}

	private Map<String, Object> listUserInfoVO(Integer pages, QueryWrapper<UserInfo> queryWrapper) {
		Page<UserInfo> userInfoPage = QueryPageUtils.getPage(new Page<>(), pages, 10, true, "id");
		Page<UserInfo> page = userInfoService.page(userInfoPage, queryWrapper);
		List<UserManageVO> userManageVOList = userService.listUserManageVO(page.getRecords());
		return DBPageUtils.getPage(page, userManageVOList);
	}

	/**
	 * 用户搜索接口
	 *
	 * @param model
	 * @param pages
	 * @param user
	 * @return
	 */
	@ApiOperation("用户搜索接口")
	@ApiImplicitParam(name = "user", value = "用户名关键词")
	@PostMapping("/user/search")
	public String search(Model model, Integer pages, String user) {
		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotEmpty(user), FieldToUnderline.to(UserInfo.Fields.nickname), user);
		model.addAttribute("page", listUserInfoVO(pages, queryWrapper));
		return "admin/user_manager::userList";
	}

	/**
	 * 封禁/取消封禁用户
	 * @param id
	 * @param status
	 * @return
	 */
	@ApiOperation("封禁用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status",value = "当前用户状态",required = true)
	})
	@GetMapping("/user/prohibit/{id}/{status}")
	public String prohibitUser(@PathVariable("id") Integer id, @PathVariable("status") String status
	) {
		UserInfo u = userInfoService.getById(id);
		u.setStatus(status);
		userInfoService.updateById(u);
		return "redirect:/admin/user";
	}

	/**
	 * 删除用户
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("删除用户")
	@GetMapping("/user/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		userInfoService.removeById(id);
		return "redirect:/admin/user";
	}
}
