package per.chao.lifeshow.controller.admin;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import per.chao.lifeshow.entity.pojo.UserReports;
import per.chao.lifeshow.entity.pojo.Videos;
import per.chao.lifeshow.entity.vo.VideoManageVO;
import per.chao.lifeshow.service.*;
import per.chao.lifeshow.utils.DBPageUtils;
import per.chao.lifeshow.utils.FieldToUnderline;
import per.chao.lifeshow.utils.QueryPageUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/4 17:24
 **/
@Api("短视频管理操作接口")
@Controller
@RequestMapping("/admin")
public class VideoManageController {
	@Autowired
	private IVideosService videosService;
	@Autowired
	@Qualifier("activServiceImpl")
	private IActivService activService;
	@Autowired
	private IUserReportsService userReportsService;
	@Autowired
	private IMessagesService messagesService;
	@Autowired
	private IUserService userService;

	/**
	 * 短视频管理页
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation("短视频管理页")
	@GetMapping("/video")
	public String video(Model model) {
		LambdaQueryWrapper<Videos> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Videos::getStatusId, 1).or(lqw -> {
			lqw.eq(Videos::getStatusId, 2);
		});
		model.addAttribute("page", listVideoVO(1, wrapper, false));
		model.addAttribute("activs", activService.list());
		return "admin/video_manager";
	}

	private Map<String, Object> listVideoVO(Integer pages, Wrapper<Videos> queryWrapper, Boolean external) {
		Page<Videos> videosPage = QueryPageUtils.getPage(new Page<>(), pages, 10, true, "id");
		Page<Videos> page = videosService.page(videosPage, queryWrapper);
		List<VideoManageVO> userManageVOList = videosService.listVideoManageVO(page.getRecords(), external);
		return DBPageUtils.getPage(page, userManageVOList);
	}

	/**
	 * 短视频搜索接口
	 *
	 * @param model
	 * @param pages
	 * @param title
	 * @param activId
	 * @return
	 */
	@ApiOperation("短视频搜索接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "title", value = "短视频标题关键词"),
			@ApiImplicitParam(name = "activId", value = "短视频相关活动id")
	})
	@PostMapping("/video/search")
	public String search(Model model, Integer pages, String title, Integer activId) {
		LambdaQueryWrapper<Videos> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtils.isNotEmpty(title), Videos::getVideoTitle, title).and(activId != null, lqw -> {
			lqw.eq(Videos::getActivId, activId);
		}).and(lqw -> {
			lqw.eq(Videos::getStatusId, 1).or(lqw1 -> {
				lqw1.eq(Videos::getStatusId, 2);
			});
		});
		model.addAttribute("page", listVideoVO(pages, wrapper, false));
		return "admin/video_manager::videoList";
	}

	/**
	 * 短视频删除接口
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/video/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		Videos video = videosService.getById(id);
		File v = new File(video.getVideoPath());
		File cover = new File(video.getCoverPath());
		v.delete();
		cover.delete();
		userService.decreaseWorks(video.getCreatedBy(),1);
		videosService.removeById(id);
		return "redirect:/admin/video";
	}

	/**
	 * 举报模块页面
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation("举报模块页面")
	@GetMapping("/report")
	public String report(Model model) {
		LambdaQueryWrapper<Videos> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Videos::getStatusId, 2).or(lqw -> {
			lqw.eq(Videos::getStatusId, 3);
		});
		model.addAttribute("page", listVideoVO(1, wrapper, true));
		model.addAttribute("activs", activService.list());
		return "admin/report_manager";
	}

	/**
	 * 举报模块搜索接口
	 *
	 * @param model
	 * @param pages
	 * @param title
	 * @param activId
	 * @return
	 */
	@ApiOperation("举报模块搜索接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "title", value = "短视频标题关键词"),
			@ApiImplicitParam(name = "activId", value = "短视频相关活动id")
	})
	@PostMapping("/report/search")
	public String reportSearch(Model model, Integer pages, String title, Integer activId) {
		LambdaQueryWrapper<Videos> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StringUtils.isNotEmpty(title), Videos::getVideoTitle, title).and(activId != null, lqw -> {
			lqw.eq(Videos::getActivId, activId);
		}).and(lqw -> {
			lqw.eq(Videos::getStatusId, 2).or(lqw1 -> {
				lqw1.eq(Videos::getStatusId, 3);
			});
		});
		model.addAttribute("page", listVideoVO(pages, wrapper, true));
		return "admin/report_manager::videoList";
	}

	/**
	 * 封禁短视频接口
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("封禁短视频接口")
	@GetMapping("/report/prohibit/{id}")
	public String prohibitVideo(@PathVariable("id") Integer id) {
		updateReportStatus(id);
		Videos v = videosService.getById(id);
		v.setStatusId(3);
		videosService.updateById(v);
		messagesService.saveProhibitionMessage(v.getVideoTitle(), v.getCreatedBy());
		return "redirect:/admin/report";
	}

	/**
	 * 忽略举报操作接口
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("忽略举报操作接口")
	@GetMapping("/report/ignore/{id}")
	public String ignoreReport(@PathVariable("id") Integer id) {
		updateReportStatus(id);
		Videos v = videosService.getById(id);
		v.setStatusId(1);
		videosService.updateById(v);
		return "redirect:/admin/report";
	}

	private void updateReportStatus(@PathVariable("id") Integer id) {
		QueryWrapper<UserReports> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(UserReports.Fields.videoId), id);
		List<UserReports> reports = userReportsService.list(wrapper);
		reports.forEach(r -> r.setResult("1"));
		userReportsService.updateBatchById(reports);
	}
}
