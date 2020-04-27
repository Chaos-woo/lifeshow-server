package per.chao.lifeshow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import per.chao.lifeshow.entity.pojo.*;
import per.chao.lifeshow.entity.vo.UserInfoVO;
import per.chao.lifeshow.entity.vo.UserManageVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/18 20:39
 **/
public interface IUserService {
	Map<String, String> wxCode2Session(String code);

	boolean saveUserInfo(UserInfoVO userInfoVO);

	UserStat getUserStat(Integer id);

	Map<String,String> getUserId(String auth);

	Map<String,Object> listFollowers(Page<UserFollowers> page);

	Map<String, Object> listBlacklist(Page<UserBlacklist> page);

	void decreaseWorks(Integer id,Integer count);

	Map<String, Object> listFans(Page<UserFans> page);

	Map<String, Object> searchUser(Page<UserInfo> page);

	void delete(Integer type,Integer id,Integer removedId);

	UserInfoVO getUserInfo(Integer id,Integer otherId);

	boolean handleFollowed(Integer id,Integer otherId,Boolean isFollowed);

	List<UserManageVO> listUserManageVO(Collection<UserInfo> userInfos);
}
