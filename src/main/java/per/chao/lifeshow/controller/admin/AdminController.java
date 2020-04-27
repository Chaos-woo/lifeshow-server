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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.entity.pojo.AdminAuths;
import per.chao.lifeshow.entity.pojo.AdminInfo;
import per.chao.lifeshow.entity.vo.AdminInfoVO;
import per.chao.lifeshow.service.IAdminAuthService;
import per.chao.lifeshow.service.IAdminInfoService;
import per.chao.lifeshow.service.IAdminService;
import per.chao.lifeshow.service.ISysAuthsService;
import per.chao.lifeshow.utils.CookieUtils;
import per.chao.lifeshow.utils.DBPageUtils;
import per.chao.lifeshow.utils.FieldToUnderline;
import per.chao.lifeshow.utils.QueryPageUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/29 15:43
 **/
@Api("管理员操作接口")
@Controller
@RequestMapping("/")
public class AdminController {
	@Autowired
	private IAdminService adminService;
	@Autowired
	private IAdminAuthService adminAuthService;
	@Autowired
	private IAdminInfoService adminInfoService;
	@Autowired
	private ISysAuthsService sysAuthsService;

	/**
	 * 管理员登录页面
	 *
	 * @return
	 */
	@ApiOperation("管理员登录页面")
	@GetMapping
	public String loginPage() {
		return "admin/login";
	}

	/**
	 * 系统首页
	 *
	 * @return
	 */
	@ApiOperation("系统首页")
	@GetMapping("admin/dashboard")
	public String dashboardPage() {
		return "admin/dashboard";
	}

	/**
	 * 管理员登录接口
	 *
	 * @param admin
	 * @param password
	 * @param auto
	 * @param session
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@ApiOperation("管理员登录接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "admin", value = "管理员帐户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "password", value = "管理员登录口令", required = true, dataType = "String"),
			@ApiImplicitParam(name = "auto", value = "自动登录选项")
	})
	@PostMapping("admin/login")
	public String login(@RequestParam String admin, @RequestParam String password, boolean auto,
						HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		// 验证用户名和密码（是否保存登录缓存）
		AdminInfoVO adminInfoVO = adminService.verifyAdminAuthAndCache(admin, password, auto);
		if (adminInfoVO != null) {
			if (auto) {
				Integer expire = 60 * 60 * 24 * 7;
				CookieUtils.setCookie(response, "auto", adminInfoVO.getSessionKey(), expire, true, "/admin");
			}
			session.setAttribute("admin", adminInfoVO);
			// 页面跳转：首页
			return "redirect:/admin/dashboard";
		}
		// 页面跳转：重新登录
		redirectAttributes.addFlashAttribute("message", "帐户名或密码错误");
		return "redirect:/admin";
	}

	/**
	 * 管理员登录口令更新
	 *
	 * @param password
	 * @param session
	 * @return
	 */
	@ApiOperation("管理员登录口令更新")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "password", value = "新的登录口令", required = true, dataType = "String")
	})
	@PostMapping("admin/pwd")
	@ResponseBody
	public JsonResult updatePassword(@RequestParam String password, HttpSession session) {
		AdminInfoVO admin = (AdminInfoVO) session.getAttribute("admin");
		if (password == null || StringUtils.equals(StringUtils.trim(password), "")) {
			return JsonResult.error("");
		} else {
			AdminInfoVO adminInfoVO = adminService.updatePassword(admin, password);
			session.setAttribute("admin", adminInfoVO);
			return JsonResult.ok("");
		}
	}

	/**
	 * 注销登录状态
	 *
	 * @param session
	 * @param response
	 * @return
	 */
	@ApiOperation("注销登录状态")
	@GetMapping("admin/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		session.removeAttribute("admin");
		CookieUtils.clearCookie(response, "auto", "/admin");
		return "redirect:/admin";
	}

	/**
	 * 系统管理员管理接口-页面
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation("系统管理员管理接口-页面")
	@GetMapping("admin/sys_admin")
	public String admin(Model model) {
		model.addAttribute("page", listAdminInfoVO(1, new QueryWrapper<>()));
		model.addAttribute("auths", sysAuthsService.list());
		return "admin/sys_admin_manager";
	}

	/**
	 * 管理员信息页面封装
	 *
	 * @param pages
	 * @param queryWrapper
	 * @return
	 */
	private Map<String, Object> listAdminInfoVO(Integer pages, QueryWrapper<AdminAuths> queryWrapper) {
		Page<AdminAuths> adminAuthsPage = QueryPageUtils.getPage(new Page<>(), pages, 10, true, "id");
		Page<AdminAuths> pageAuths = adminAuthService.page(adminAuthsPage, queryWrapper);
		List<AdminInfoVO> records = adminService.listAdminInfoVOByAuths(pageAuths.getRecords());
		return DBPageUtils.getPage(pageAuths, records);
	}

	/**
	 * 管理员帐户搜索
	 *
	 * @param model
	 * @param pages
	 * @param admin
	 * @return
	 */
	@ApiOperation("管理员帐户搜索")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pages", value = "请求页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "admin", value = "搜索关键词", dataType = "String")
	})
	@PostMapping("admin/sys_admin/search")
	public String search(Model model, Integer pages, String admin) {
		QueryWrapper<AdminAuths> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotEmpty(admin), "account", admin);
		model.addAttribute("page", listAdminInfoVO(pages, queryWrapper));
		return "admin/sys_admin_manager::adminList";
	}

	/**
	 * 新增新系统管理员/更新管理员信息
	 *
	 * @param adminInfoVO
	 * @param redirectAttributes
	 * @return
	 */
	@ApiOperation("新增新系统管理员/更新管理员信息")
	@ApiImplicitParam(name = "AdminInfoVO", value = "管理员信息VO")
	@PostMapping("admin/sys_admin")
	public String post(@Valid AdminInfoVO adminInfoVO, RedirectAttributes redirectAttributes) {
		QueryWrapper<AdminInfo> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(AdminInfo.Fields.nickname), adminInfoVO.getAccount());
		AdminInfo adminInfo1 = adminInfoService.getOne(wrapper);
		if (adminInfo1 != null) {
			redirectAttributes.addFlashAttribute("message", "系统管理员" + adminInfoVO.getAccount() + "已经存在");
		} else {
			if (saveOrUpdate(adminService.VO2AdminInfo(adminInfoVO), adminService.VO2AdminAuths(adminInfoVO))) {
				redirectAttributes.addFlashAttribute("message", "操作成功");
			} else {
				redirectAttributes.addFlashAttribute("message", "操作失败");
			}
		}
		return "redirect:/admin/sys_admin";
	}

	/**
	 * 新增/更新接口封装
	 *
	 * @param admin
	 * @param auths
	 * @return
	 */
	private boolean saveOrUpdate(AdminInfo admin, AdminAuths auths) {
		Integer idBack = admin.getId();
		adminInfoService.saveOrUpdate(admin);
		if (idBack == null) {
			// 新增
			QueryWrapper<AdminInfo> wrapper = new QueryWrapper<>();
			wrapper.eq(FieldToUnderline.to(AdminInfo.Fields.nickname), admin.getNickname());
			AdminInfo adminInfo = adminInfoService.getOne(wrapper);
			auths.setAdminId(adminInfo.getId());
		} else {
			// 更新
			QueryWrapper<AdminAuths> wrapper = new QueryWrapper<>();
			wrapper.eq(FieldToUnderline.to(AdminAuths.Fields.adminId), admin.getId());
			AdminAuths adminAuths = adminAuthService.getOne(wrapper);
			auths.setId(adminAuths.getId());
		}
		adminAuthService.saveOrUpdate(auths);
		return true;
	}

	/**
	 * 管理员信息获取
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("管理员信息获取")
	@ApiImplicitParam(name = "id", value = "管理员id", required = true, dataType = "Integer")
	@GetMapping("admin/sys_admin/edit")
	@ResponseBody
	public JsonResult getAdminInfo(@RequestParam Integer id) {
		Page<AdminAuths> adminAuthsPage = QueryPageUtils.getPage(new Page<>(), 1, 10, true, "id");
		QueryWrapper<AdminAuths> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(AdminAuths.Fields.adminId), id);
		Page<AdminAuths> pageAuths = adminAuthService.page(adminAuthsPage, wrapper);
		List<AdminInfoVO> records = adminService.listAdminInfoVOByAuths(pageAuths.getRecords());
		return JsonResult.ok(records.get(0));
	}

	/**
	 * 管理员删除操作
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("管理员删除操作")
	@ApiImplicitParam(name = "id", value = "被删除管理员id", required = true, dataType = "Integer")
	@GetMapping("admin/sys_admin/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		adminService.deleteAdmin(id);
		return "redirect:/admin/sys_admin";
	}


}
