package per.chao.lifeshow.service;

import per.chao.lifeshow.entity.pojo.AdminAuths;
import per.chao.lifeshow.entity.pojo.AdminInfo;
import per.chao.lifeshow.entity.vo.AdminInfoVO;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/28 14:54
 **/
public interface IAdminService {
	AdminInfoVO verifyAdminAuthAndCache(String admin, String password, boolean auto);

	AdminInfoVO updatePassword(AdminInfoVO admin, String password);

	AdminInfoVO verifySessionKey(String sessionKey);

	List<AdminInfoVO> listAdminInfoVOByAuths(Collection<AdminAuths> auths);

	Boolean deleteAdmin(Integer id);

	AdminInfo VO2AdminInfo(AdminInfoVO adminInfoVO);

	AdminAuths VO2AdminAuths(AdminInfoVO adminInfoVO);

}
