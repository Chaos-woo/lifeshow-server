package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.model.WXSessionModel;
import per.chao.lifeshow.entity.pojo.*;
import per.chao.lifeshow.entity.vo.SearchUserVO;
import per.chao.lifeshow.entity.vo.UserInfoVO;
import per.chao.lifeshow.entity.vo.UserManageVO;
import per.chao.lifeshow.mapper.*;
import per.chao.lifeshow.service.IUserService;
import per.chao.lifeshow.utils.*;

import java.util.*;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/16 20:19
 **/
@Service
public class UserServiceImpl implements IUserService {
	// 用户状态，0：正常 1：封禁
	public static final String USER_DEFAULT_STATUS = "0";
	public static final String USER_CLOSED_STATUS = "1";
	// 登录状态，1：已登录 0：未登录
	public static final String USER_LOGIN_STATUS = "0";
	public static final String USER_LOGINED_STATUS = "1";
	// 用户性别，0：未知、1：男、2：女
	public static final String GENDER_UNKNOWN = "0";
	public static final String GENDER_MALE = "1";
	public static final String GENDER_FEMALE = "0";

	@Value("${wx.appid}")
	private String appid;
	@Value("${wx.secret}")
	private String secret;
	@Value("${wx.requestUrl}")
	private String url;
	@Value("${wx.grantType}")
	private String grantType;

	@Autowired
	private UserAuthsMapper userAuthsMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserStatMapper userStatMapper;
	@Autowired
	private UserFollowersMapper userFollowersMapper;
	@Autowired
	private UserBlacklistMapper userBlacklistMapper;
	@Autowired
	private UserFansMapper userFansMapper;


	public Map<String, String> wxCode2Session(String code) {
		WXSessionModel wxSessionModel = getWxSessionModel(code);
		if (wxSessionModel != null) {
			String password = MD5Utils.getMd5(System.currentTimeMillis() + code + wxSessionModel.getSessionKey());
			// 数据库存储
			Integer id = initUserInfoAndAuth(password, wxSessionModel.getOpenid());
			if (id != -1) {
				// 存入缓存redis


				Map<String, String> data = new HashMap<>();
				data.put("password", password);
				data.put("id", String.valueOf(id));
				return data;
			}
		}
		return null;
	}

	private Integer initUserInfoAndAuth(String password, String openid) {
		Integer id = userInfoMapper.selectIdByOpenid(openid);
		if (id == null) {
			UserInfo u = new UserInfo();
			u.setGender(GENDER_UNKNOWN);
			u.setRegisteredDate(System.currentTimeMillis());
			u.setStatus(USER_DEFAULT_STATUS);
			u.setOpenid(openid);
			userInfoMapper.insert(u);

			UserAuths ua = new UserAuths();
			ua.setCredential(password);
			ua.setOpenid(openid);
			ua.setStatus(USER_LOGINED_STATUS);
			ua.setLastLoginDate(System.currentTimeMillis());
			Integer userId = userInfoMapper.selectIdByOpenid(openid);
			ua.setUserId(userId);
			userAuthsMapper.insert(ua);

			UserStat us = new UserStat();
			us.setFansCount(0);
			us.setFollowersCount(0);
			us.setReceivedLikedCount(0);
			us.setUserId(userId);
			userStatMapper.insert(us);

			return userId;
		}
		QueryWrapper<UserAuths> w = new QueryWrapper<>();
		w.eq(FieldToUnderline.to(UserAuths.Fields.userId),id);
		UserAuths updateAuths = userAuthsMapper.selectOne(w);
		updateAuths.setCredential(password);
		updateAuths.setLastLoginDate(System.currentTimeMillis());
		userAuthsMapper.updateById(updateAuths);
		return id;
	}

	private WXSessionModel getWxSessionModel(String code) {
		Map<String, String> param = new HashMap<>();
		param.put("appid", appid);
		param.put("secret", secret);
		param.put("js_code", code);
		param.put("grant_type", grantType);
		String wxResult = HttpClientUtils.doGet(url, param);
		return JsonUtils.json2Pojo(wxResult, WXSessionModel.class);
	}

	@Override
	public boolean saveUserInfo(UserInfoVO vo) {
		if (vo != null) {
			UserInfo userInfo = userInfoMapper.selectById(vo.getId());
			userInfo.setNickname(vo.getNickName());
			userInfo.setGender(vo.getGender());
			userInfo.setAvatar(vo.getAvatarUrl());
			userInfo.setCountry(vo.getCountry());
			userInfo.setProvince(vo.getProvince());
			userInfo.setCity(vo.getCity());
			userInfoMapper.updateById(userInfo);
			return true;
		}
		return false;
	}

	@Override
	public UserStat getUserStat(Integer id) {
		UserStat us = userStatMapper.selectByUserId(id);
		us.setId(-1);
		return us;
	}

	@Override
	public Map<String, String> getUserId(String auth) {
		Integer id = userAuthsMapper.selectUserIdByAuth(auth);
		if (id != null) {
			Map<String, String> data = new HashMap<>();
			data.put("id", String.valueOf(id));
			return data;
		}
		return null;
	}

	@Override
	public void decreaseWorks(Integer id,Integer count){
		UserStat userStat = userStatMapper.selectByUserId(id);
		userStat.setWorksCount(userStat.getWorksCount() - count);
		userStatMapper.updateById(userStat);
	}

	@Override
	public Map<String, Object> listFollowers(Page<UserFollowers> page) {
		List<SearchUserVO> searchUserVOList = new ArrayList<>();
		List<UserFollowers> records = page.getRecords();
		records.forEach(f -> {
			UserInfo userInfo = userInfoMapper.selectById(f.getFollowedUser());
			UserStat userStat = userStatMapper.selectByUserId(f.getFollowedUser());
			SearchUserVO vo = processSearchUserVO(userInfo, userStat);
			Map<String, String> external = new HashMap<>();
			external.put("followedDate", DateFormatter.to(f.getFollowedDate()));
			external.put("status", f.getStatus());
			vo.setExternal(external);
			searchUserVOList.add(vo);
		});
		return DBPageUtils.getPage(page, searchUserVOList);
	}

	@Override
	public Map<String, Object> listFans(Page<UserFans> page) {
		List<SearchUserVO> searchUserVOList = new ArrayList<>();
		List<UserFans> records = page.getRecords();
		records.forEach(f -> {
			UserInfo userInfo = userInfoMapper.selectById(f.getFansId());
			UserStat userStat = userStatMapper.selectByUserId(f.getFansId());
			SearchUserVO vo = processSearchUserVO(userInfo, userStat);
			Map<String, String> external = new HashMap<>();
			external.put("followedDate", DateFormatter.to(f.getFollowedDate()));
			external.put("status", f.getStatus());
			vo.setExternal(external);
			searchUserVOList.add(vo);
		});
		return DBPageUtils.getPage(page, searchUserVOList);
	}

	@Override
	public Map<String, Object> searchUser(Page<UserInfo> page) {
		List<SearchUserVO> searchUserVOList = new ArrayList<>();
		List<UserInfo> records = page.getRecords();
		records.forEach(u -> {
			UserStat userStat = userStatMapper.selectByUserId(u.getId());
			SearchUserVO vo = processSearchUserVO(u, userStat);
			searchUserVOList.add(vo);
		});
		return DBPageUtils.getPage(page, searchUserVOList);
	}

	private SearchUserVO processSearchUserVO(UserInfo userInfo, UserStat userStat) {
		SearchUserVO vo = new SearchUserVO();
		vo.setId(userInfo.getId());
		vo.setNickName(userInfo.getNickname());
		vo.setAvatarUrl(userInfo.getAvatar());
		vo.setFans(NumberFormatterCustom.to(userStat.getFansCount()));
		vo.setWorks(NumberFormatterCustom.to(userStat.getWorksCount()));
		return vo;
	}

	@Override
	public Map<String, Object> listBlacklist(Page<UserBlacklist> page) {
		List<SearchUserVO> searchUserVOList = new ArrayList<>();
		List<UserBlacklist> records = page.getRecords();
		records.forEach(f -> {
			UserInfo userInfo = userInfoMapper.selectById(f.getBlackUserId());
			UserStat userStat = userStatMapper.selectByUserId(f.getBlackUserId());
			SearchUserVO vo = processSearchUserVO(userInfo, userStat);
			searchUserVOList.add(vo);
		});
		return DBPageUtils.getPage(page, searchUserVOList);
	}

	@Override
	public void delete(Integer type, Integer id, Integer removedId) {
		if (type == 0) {
			LambdaQueryWrapper<UserFollowers> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(UserFollowers::getUserId, id)
					.eq(UserFollowers::getFollowedUser, removedId);
			userFollowersMapper.delete(wrapper);
		} else {
			LambdaQueryWrapper<UserBlacklist> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(UserBlacklist::getUserId, id)
					.eq(UserBlacklist::getBlackUserId, removedId);
			userBlacklistMapper.delete(wrapper);
		}
	}

	@Override
	public UserInfoVO getUserInfo(Integer id, Integer otherId) {
		UserInfo u = userInfoMapper.selectById(otherId);
		if (u != null) {
			UserInfoVO vo = new UserInfoVO();
			vo.setId(u.getId());
			vo.setAvatarUrl(u.getAvatar());
			vo.setCity(u.getCity());
			vo.setCountry(u.getCountry());
			vo.setGender(u.getGender());
			vo.setNickName(u.getNickname());
			vo.setProvince(u.getProvince());
			Map<String, Object> external = new HashMap<>();
			external.put("isFollowed", isFollowed(id, otherId));
			external.put("disabled", isBlacklist(id, otherId));
			vo.setExternal(external);
			return vo;
		}
		return null;
	}

	private boolean isFollowed(Integer id, Integer otherId) {
		LambdaQueryWrapper<UserFollowers> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserFollowers::getUserId, id).eq(UserFollowers::getFollowedUser, otherId);
		return userFollowersMapper.selectOne(wrapper) != null;
	}

	private boolean isBlacklist(Integer id, Integer otherId) {
		LambdaQueryWrapper<UserBlacklist> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserBlacklist::getUserId, id).eq(UserBlacklist::getBlackUserId, otherId);
		return userBlacklistMapper.selectOne(wrapper) != null;
	}

	@Override
	public boolean handleFollowed(Integer id, Integer otherId, Boolean isFollowed) {
		LambdaQueryWrapper<UserFollowers> followerWrapper = Wrappers.lambdaQuery();
		LambdaQueryWrapper<UserFans> fansWrapper = Wrappers.lambdaQuery();
		followerWrapper.eq(UserFollowers::getUserId, otherId).eq(UserFollowers::getFollowedUser, id);
		UserFollowers followRelation = userFollowersMapper.selectOne(followerWrapper);
		fansWrapper.eq(UserFans::getFansId, otherId).eq(UserFans::getUserId, id);
		UserFans fansRelation = userFansMapper.selectOne(fansWrapper);
		if (isFollowed) {
			LambdaQueryWrapper<UserFollowers> followerWrapperDelete = Wrappers.lambdaQuery();
			LambdaQueryWrapper<UserFans> fansWrapperDelete = Wrappers.lambdaQuery();
			// 已关注状态：删除关注
			followerWrapperDelete.eq(UserFollowers::getUserId, id).eq(UserFollowers::getFollowedUser, otherId);
			userFollowersMapper.delete(followerWrapperDelete);
			if (followRelation != null) {
				followRelation.setStatus("0");
				userFollowersMapper.updateById(followRelation);
			}

			fansWrapperDelete.eq(UserFans::getFansId, id).eq(UserFans::getUserId, otherId);
			userFansMapper.delete(fansWrapperDelete);
			if (fansRelation != null) {
				fansRelation.setStatus("0");
				userFansMapper.updateById(fansRelation);
			}
		} else {
			Long followedDate = System.currentTimeMillis();

			// 处理关注状态
			UserFollowers uf = new UserFollowers();
			uf.setUserId(id);
			uf.setFollowedUser(otherId);
			uf.setFollowedDate(followedDate);
			uf.setStatus(followRelation == null ? "0" : "1");
			userFollowersMapper.insert(uf);

			if (followRelation != null) {
				followRelation.setStatus("1");
				userFollowersMapper.updateById(followRelation);
			}

			// 处理粉丝状态
			UserFans ufs = new UserFans();
			ufs.setFansId(id);
			ufs.setUserId(otherId);
			ufs.setFollowedDate(followedDate);
			ufs.setStatus(fansRelation == null ? "0" : "1");
			userFansMapper.insert(ufs);

			if (fansRelation != null) {
				fansRelation.setStatus("1");
				userFansMapper.updateById(fansRelation);
			}
		}
		return true;
	}

	@Override
	public List<UserManageVO> listUserManageVO(Collection<UserInfo> userInfos) {
		List<UserManageVO> userManageVOList = new ArrayList<>();
		userInfos.forEach(u -> userManageVOList.add(getUserStatAndAuth(u)));
		return userManageVOList;
	}

	private UserManageVO getUserStatAndAuth(UserInfo u) {
		QueryWrapper<UserAuths> authWrapper = new QueryWrapper<>();
		authWrapper.eq(FieldToUnderline.to(UserAuths.Fields.userId), u.getId());
		QueryWrapper<UserStat> statWrapper = new QueryWrapper<>();
		statWrapper.eq(FieldToUnderline.to(UserStat.Fields.userId), u.getId());
		UserAuths userAuths = userAuthsMapper.selectOne(authWrapper);
		UserStat userStat = userStatMapper.selectOne(statWrapper);
		StringBuilder stat = new StringBuilder();
		stat.append(userStat.getFollowersCount()).append("/").append(userStat.getFansCount()).append("/").append(userStat.getWorksCount());
		return new UserManageVO(u.getOpenid(), u.getId(), u.getAvatar(), u.getNickname(), u.getGender(), u.getRegisteredDate(), userAuths.getLastLoginDate(), u.getStatus(), stat.toString(), u.getCity(), u.getCountry(), u.getProvince());
	}
}
