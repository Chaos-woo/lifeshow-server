package per.chao.lifeshow.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.entity.pojo.*;
import per.chao.lifeshow.entity.vo.UserVideoVO;
import per.chao.lifeshow.service.*;
import per.chao.lifeshow.utils.FieldToUnderline;

import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/29 11:11
 **/
@Api("用户视频接口")
@RestController
@RequestMapping("/u/video")
public class UserVideoController {
	@Autowired
	private IVideosService videosService;
	@Autowired
	private ICommentsService commentsService;
	@Autowired
	private IVideoStatService videoStatService;
	@Autowired
	private IUserReportsService userReportsService;
	@Autowired
	private IDanmakuService danmakuService;
	@Autowired
	@Qualifier("activServiceImpl")
	private IActivService activService;

	/**
	 * 用户上传视频接口
	 *
	 * @param videos
	 * @param activName
	 * @param file
	 * @return
	 */
	@ApiOperation("用户上传视频接口")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "activName", name = "活动名", required = true),
			@ApiImplicitParam(value = "file", name = "视频文件", required = true)
	})
	@PostMapping("/video")
	public JsonResult uploadVideo(Videos videos, String activName, @RequestParam("file") MultipartFile file) {
		Integer videoId = videosService.uploadVideo(videos, activName, file);
		return JsonResult.ok(videoId);
	}

	/**
	 * 用户上传视频封面图接口
	 *
	 * @param videoId
	 * @param file
	 * @return
	 */
	@ApiOperation("用户上传视频封面图接口")
	@ApiImplicitParam(value = "file", name = "封面文件", required = true)
	@PostMapping("/cover")
	public JsonResult uploadCover(Integer videoId, @RequestParam("file") MultipartFile file) {
		Boolean result = videosService.uploadCover(videoId, file);
		return JsonResult.ok(result);
	}

	/**
	 * 分页获取用户作品
	 *
	 * @param id
	 * @param pages
	 * @return
	 */
	@ApiOperation("分页获取用户作品")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "id", name = "用户id", required = true),
			@ApiImplicitParam(value = "pages", name = "分页页数", required = true)
	})
	@GetMapping("/works")
	public JsonResult getUserWorks(Integer id, Integer pages) {
		Map<String, Object> userWorksList = videosService.selectByUserId(id, pages, 15);
		if (userWorksList != null) {
			return JsonResult.ok(userWorksList);
		}
		return JsonResult.error("已经没有啦~");
	}

	/**
	 * 分页获取用户收藏作品
	 *
	 * @param id
	 * @param pages
	 * @return
	 */
	@ApiOperation("分页获取用户收藏作品")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "id", name = "用户id", required = true),
			@ApiImplicitParam(value = "pages", name = "分页页数", required = true)
	})
	@GetMapping("/collectedworks")
	public JsonResult getUserCollectedWorks(Integer id, Integer pages) {
		Map<String, Object> userWorksList = videosService.selectCollectedByUserId(id, pages, 15);
		if (userWorksList != null) {
			return JsonResult.ok(userWorksList);
		}
		return JsonResult.error("已经没有啦~");
	}

	/**
	 * 分页搜索作品
	 *
	 * @param keyword
	 * @param pages
	 * @return
	 */
	@ApiOperation("分页搜索作品")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "keyword", name = "关键字", required = true),
			@ApiImplicitParam(value = "pages", name = "分页页数", required = true)
	})
	@GetMapping("/search")
	public JsonResult searchWorks(String keyword, Integer pages) {
		Map<String, Object> userWorksList = videosService.selectByKeyword(keyword, pages, 15);
		if (userWorksList != null) {
			return JsonResult.ok(userWorksList);
		}
		return JsonResult.error("已经没有啦~");
	}

	/**
	 * 随机推送短视频合集
	 *
	 * @return
	 */
	@ApiOperation("随机推送短视频合集")
	@GetMapping("/rand")
	public JsonResult getRandVideoSet(Integer id, Boolean attention) {
		return JsonResult.ok(videosService.randVideoSet(id, attention));
	}

	/**
	 * 根据活动随机推送短视频合集
	 *
	 * @return
	 */
	@ApiOperation("根据活动随机推送短视频合集")
	@GetMapping("/activ")
	public JsonResult getRandVideoSet(Integer id, Integer activ) {
		Activs activs = activService.getById(activ);
		if (activs.getActivsCount() == 0) {
			return JsonResult.error("此活动还没有短视频噢~");
		}
		return JsonResult.ok(videosService.randActivVideoSet(id, activ));
	}

	/**
	 * 获取视频留言
	 *
	 * @param id
	 * @param pages
	 * @return
	 */
	@ApiOperation("获取视频留言")
	@GetMapping("/comment")
	public JsonResult getVideoComment(Integer id, Integer pages) {
		Map<String, Object> videoComments = commentsService.getVideoComments(id, pages, 15);
		if (videoComments != null) {
			return JsonResult.ok(videoComments);
		}
		return JsonResult.error("已经没有啦~");
	}

	/**
	 * 获取单条视频信息
	 *
	 * @param id
	 * @param videoId
	 * @return
	 */
	@ApiOperation("获取单条视频信息")
	@GetMapping("/detail")
	public JsonResult getOneVideo(Integer id, Integer videoId) {
		UserVideoVO oneVideoDetail = videosService.getOneVideoDetail(id, videoId);
		if (oneVideoDetail != null) {
			return JsonResult.ok(oneVideoDetail);
		}
		return JsonResult.error("当前视频不存在...");
	}

	/**
	 * 保存视频留言
	 *
	 * @param comments
	 * @return
	 */
	@ApiOperation("保存视频留言")
	@PostMapping("/comment")
	public JsonResult postVideoComment(Comments comments) {
		QueryWrapper<Comments> wrapperCommentCount = new QueryWrapper<>();
		wrapperCommentCount.eq(FieldToUnderline.to(Comments.Fields.videoId), comments.getVideoId());
		if (commentsService.count(wrapperCommentCount) <= 100) {
			comments.setCreatedAt(System.currentTimeMillis());
			commentsService.save(comments);
			QueryWrapper<VideoStat> wrapper = new QueryWrapper<>();
			wrapper.eq(FieldToUnderline.to(VideoStat.Fields.videoId), comments.getVideoId());
			VideoStat stat = videoStatService.getOne(wrapper);
			stat.setCommentCount(stat.getCommentCount() + 1);
			videoStatService.updateById(stat);
		}
		return JsonResult.okNonData();
	}

	/**
	 * 保存视频弹幕
	 *
	 * @param danmaku
	 * @return
	 */
	@ApiOperation("保存视频弹幕")
	@PostMapping("/danmaku")
	public JsonResult postVideoDanmaku(Danmaku danmaku) {
		QueryWrapper<Danmaku> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(Danmaku.Fields.videoId), danmaku.getVideoId());
		if (danmakuService.count(wrapper) <= 40) {
			danmakuService.save(danmaku);
		}
		return JsonResult.okNonData();
	}

	/**
	 * 处理用户点赞行为
	 *
	 * @param videoId
	 * @param id
	 * @param isLiked
	 * @return
	 */
	@ApiOperation("处理用户点赞行为")
	@GetMapping("/liked")
	public JsonResult handleLiked(Integer videoId, Integer id, Boolean isLiked) {
		videosService.handleLiked(videoId, id, isLiked);
		return JsonResult.okNonData();
	}

	/**
	 * 处理用户收藏行为
	 *
	 * @param videoId
	 * @param id
	 * @param isCollected
	 * @return
	 */
	@ApiOperation("处理用户收藏行为")
	@GetMapping("/collected")
	public JsonResult handleCollected(Integer videoId, Integer id, Boolean isCollected) {
		videosService.handleCollected(videoId, id, isCollected);
		return JsonResult.okNonData();
	}

	/**
	 * 保存用户举报信息
	 *
	 * @param userReports
	 * @return
	 */
	@ApiOperation("保存用户举报信息")
	@PostMapping("/report")
	public JsonResult videoReport(UserReports userReports) {
		userReports.setCreatedAt(System.currentTimeMillis());
		userReports.setResult("0");
		userReportsService.save(userReports);
		Videos videos = videosService.getById(userReports.getVideoId());
		videos.setStatusId(2);
		videosService.updateById(videos);
		return JsonResult.okNonData();
	}
}
