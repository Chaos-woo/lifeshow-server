package per.chao.lifeshow.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.entity.pojo.*;
import per.chao.lifeshow.entity.vo.UserInfoVO;
import per.chao.lifeshow.service.*;
import per.chao.lifeshow.utils.FieldToUnderline;
import per.chao.lifeshow.utils.QueryPageUtils;

import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/15 14:30
 **/
@Api("用户操作接口")
@RestController
@RequestMapping("/u/user")
public class UserController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserFollowersService userFollowersService;
	@Autowired
	private IUserBlacklistService userBlacklistService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IUserFansService userFansService;
	@Autowired
	private ICommentsService commentsService;

	/**
	 * 根据用户小程序code获取session_key并设置登录口令
	 *
	 * @param code
	 * @return
	 */
	@ApiOperation("根据用户小程序code获取session_key并设置登录口令")
	@ApiImplicitParam(name = "code", value = "小程序login()登录获取的code", required = true)
	@PostMapping("/code2session")
	public JsonResult code2Session(String code) {
		Map<String, String> data = userService.wxCode2Session(code);
		if (data != null) {
			return JsonResult.ok(data);
		}
		return JsonResult.error("登录失败");
	}

	/**
	 * 更新用户信息
	 *
	 * @param userInfoVO
	 * @return
	 */
	@ApiOperation("更新用户信息")
	@PostMapping("/info")
	public JsonResult uploadUserInfo(UserInfoVO userInfoVO) {
		if (userService.saveUserInfo(userInfoVO)) {
			return JsonResult.okNonData();
		}
		return JsonResult.error("服务期出现了一点小问题，请重新登录");
	}

	/**
	 * 获取用户的一些统计信息
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("获取用户的一些统计信息")
	@ApiImplicitParam(name = "id", value = "用户id", required = true)
	@GetMapping("/stat")
	public JsonResult getUserStat(Integer id) {
		UserStat us = userService.getUserStat(id);
		if (us != null) {
			return JsonResult.ok(us);
		}
		return JsonResult.error("服务期出现了一点小问题，请重新登录");
	}

	/**
	 * 获取用户信息
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("获取用户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "主动获取的用户id", required = true),
			@ApiImplicitParam(name = "otherId", value = "被获取信息的用户id", required = true)
	})
	@GetMapping("/info")
	public JsonResult getUserInfo(Integer id, Integer otherId) {
		UserInfoVO u = userService.getUserInfo(id, otherId);
		if (u != null) {
			return JsonResult.ok(u);
		}
		return JsonResult.error("服务期出现了一点小问题");
	}

	/**
	 * 通过登录口令反查用户id
	 *
	 * @param auth
	 * @return
	 */
	@ApiOperation("通过登录口令反查用户id")
	@ApiImplicitParam(name = "auth", value = "登录口令", required = true)
	@GetMapping("/id")
	public JsonResult getUserId(String auth) {
		Map<String, String> data = userService.getUserId(auth);
		if (data != null) {
			return JsonResult.ok(data);
		}
		return JsonResult.error("服务期出现了一点小问题，请重新登录");
	}

	/**
	 * 分页查询某位用户的关注用户
	 *
	 * @param id
	 * @param pages
	 * @return
	 */
	@ApiOperation("分页查询某位用户的关注用户")
	@ApiImplicitParam(name = "id", value = "主动获取的用户id", required = true)
	@GetMapping("/follow")
	public JsonResult listFollowers(Integer id, Integer pages) {
		Page<UserFollowers> followers = QueryPageUtils.getPage(new Page<>(), pages, 15, true, FieldToUnderline.to(UserFollowers.Fields.followedDate));
		QueryWrapper<UserFollowers> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(UserFollowers.Fields.userId), id);
		Page<UserFollowers> followersPage = userFollowersService.page(followers, wrapper);
		if (followersPage.getRecords().size() < 1) {
			return JsonResult.okNonData();
		}
		Map<String, Object> page = userService.listFollowers(followersPage);
		return JsonResult.ok(page);
	}

	/**
	 * 分页查询某位用户的粉丝用户
	 *
	 * @param id
	 * @param pages
	 * @return
	 */
	@ApiOperation("分页查询某位用户的粉丝用户")
	@ApiImplicitParam(name = "id", value = "主动获取的用户id", required = true)
	@GetMapping("/fans")
	public JsonResult listFans(Integer id, Integer pages) {
		Page<UserFans> fans = QueryPageUtils.getPage(new Page<>(), pages, 15, true, FieldToUnderline.to(UserFans.Fields.followedDate));
		QueryWrapper<UserFans> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(UserFans.Fields.userId), id);
		Page<UserFans> fansPage = userFansService.page(fans, wrapper);
		if (fansPage.getRecords().size() < 1) {
			return JsonResult.okNonData();
		}
		Map<String, Object> page = userService.listFans(fansPage);
		return JsonResult.ok(page);
	}

	/**
	 * 分页搜索用户
	 *
	 * @param keyword
	 * @param pages
	 * @return
	 */
	@ApiOperation("分页搜索用户")
	@ApiImplicitParam(name = "keyword", value = "关键词", required = true)
	@GetMapping("/search")
	public JsonResult searchUser(String keyword, Integer pages) {
		Page<UserInfo> user = QueryPageUtils.getPage(new Page<>(), pages, 15, true, FieldToUnderline.to(UserInfo.Fields.id));
		QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
		wrapper.like(StringUtils.isNotEmpty(keyword),FieldToUnderline.to(UserInfo.Fields.nickname),keyword);
		Page<UserInfo> userPage = userInfoService.page(user, wrapper);
		if (userPage.getRecords().size() < 1) {
			return JsonResult.okNonData();
		}
		Map<String, Object> page = userService.searchUser(userPage);
		return JsonResult.ok(page);
	}

	/**
	 * 分页查询某位用户的黑名单用户
	 *
	 * @param id
	 * @param pages
	 * @return
	 */
	@ApiOperation("分页查询某位用户的关注用户")
	@ApiImplicitParam(name = "id", value = "主动获取的用户id", required = true)
	@GetMapping("/blacklist")
	public JsonResult listBlacklist(Integer id, Integer pages) {
		Page<UserBlacklist> blacklist = QueryPageUtils.getPage(new Page<>(), pages, 15, true, "id");
		QueryWrapper<UserBlacklist> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(UserBlacklist.Fields.userId), id);
		Page<UserBlacklist> blacklistPage = userBlacklistService.page(blacklist, wrapper);
		if (blacklistPage.getRecords().size() < 1) {
			return JsonResult.okNonData();
		}
		Map<String, Object> page = userService.listBlacklist(blacklistPage);
		return JsonResult.ok(page);
	}

	/**
	 * 移除某位用户的关注/黑名单用户
	 *
	 * @param type
	 * @param id
	 * @param removedId
	 */
	@ApiOperation("移除某位用户的关注/黑名单用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "移除的列表类型（0：关注列表；1：黑名单）", required = true),
			@ApiImplicitParam(name = "id", value = "主动用户id", required = true),
			@ApiImplicitParam(name = "removedId", value = "被移除用户id", required = true)
	})
	@GetMapping("/delete")
	public void delete(Integer type, Integer id, Integer removedId) {
		userService.delete(type, id, removedId);
	} // todo ： 没有处理关注类型下，单方/互相关注的情况分开

	/**
	 * 处理用户的关注/取消关注操作
	 *
	 * @param id
	 * @param otherId
	 * @param isFollowed
	 * @return
	 */
	@ApiOperation("处理用户的关注/取消关注操作")
	@ApiImplicitParam(name = "isFollowed", value = "是否已关注", required = true)
	@PostMapping("/follow")
	public JsonResult handleFollowed(Integer id, Integer otherId, Boolean isFollowed) {
		if (userService.handleFollowed(id, otherId, isFollowed)) {
			return JsonResult.okNonData();
		}
		return JsonResult.error("服务期出现了一点小问题，请重新登录");
	}

	/**
	 * 处理用户的关注/取消关注操作
	 *
	 * @param id
	 * @param otherId
	 * @return
	 */
	@ApiOperation("处理用户的拉黑操作")
	@PostMapping("/blacklist")
	public void handleBlacklist(Integer id, Integer otherId) {
		UserBlacklist ub = new UserBlacklist();
		ub.setUserId(id);
		ub.setBlackUserId(otherId);
		userBlacklistService.save(ub);
	}

	/**
	 * 获取对应视频评论
	 * @param id
	 * @param pages
	 * @return
	 */
	@ApiOperation("获取对应视频评论")
	@ApiImplicitParam(name = "id",value = "视频id",required = true)
	@GetMapping("/comment")
	public JsonResult getVideoComment(Integer id,Integer pages){
		Map<String, Object> videoComments = commentsService.getUserComments(id, pages, 15);
		if (videoComments != null) {
			return JsonResult.ok(videoComments);
		}
		return JsonResult.error("已经没有啦~");
	}
}
