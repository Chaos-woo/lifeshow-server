package per.chao.lifeshow.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.service.IStatService;
import per.chao.lifeshow.utils.RedisUtils;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/26 23:20
 **/
@RestController
@RequestMapping("/admin/stat")
public class StatController {
	@Value("${minip.access}")
	private String miniProgramAccess;
	@Value("${minip.access.now}")
	private String miniProgramAccessNow;

	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private IStatService statService;

	/**
	 * 访问量统计
	 *
	 * @return
	 */
	@GetMapping("accessTotal")
	public JsonResult accessTotal() {
		return JsonResult.ok(redisUtils.get(miniProgramAccess));
	}

	/**
	 * 今日访问量统计
	 *
	 * @return
	 */
	@GetMapping("accessNow")
	public JsonResult accessNow() {
		return JsonResult.ok(redisUtils.get(miniProgramAccessNow));
	}

	/**
	 * 音乐数量统计
	 *
	 * @return
	 */
	@GetMapping("music")
	public JsonResult music() {
		return JsonResult.ok(statService.music());
	}

	/**
	 * 总用户数统计
	 *
	 * @return
	 */
	@GetMapping("users")
	public JsonResult users() {
		return JsonResult.ok(statService.users());
	}

	/**
	 * 总短视频数统计
	 *
	 * @return
	 */
	@GetMapping("videos")
	public JsonResult videos() {
		return JsonResult.ok(statService.videos());
	}

	/**
	 * 7日用户统计
	 *
	 * @return
	 */
	@GetMapping("user")
	public JsonResult user() {
		return JsonResult.ok(statService.user());
	}

	/**
	 * 7日短视频统计
	 *
	 * @return
	 */
	@GetMapping("video")
	public JsonResult video() {
		return JsonResult.ok(statService.video());
	}
}
