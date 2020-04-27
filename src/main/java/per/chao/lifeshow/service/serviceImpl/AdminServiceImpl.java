package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import per.chao.lifeshow.entity.pojo.AdminAuths;
import per.chao.lifeshow.entity.pojo.AdminInfo;
import per.chao.lifeshow.entity.pojo.SysAuths;
import per.chao.lifeshow.entity.vo.AdminInfoVO;
import per.chao.lifeshow.mapper.AdminAuthsMapper;
import per.chao.lifeshow.mapper.AdminInfoMapper;
import per.chao.lifeshow.mapper.SysAuthsMapper;
import per.chao.lifeshow.service.IAdminService;
import per.chao.lifeshow.utils.MD5Utils;
import per.chao.lifeshow.utils.RedisUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/28 14:54
 **/
@Service
public class AdminServiceImpl implements IAdminService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AdminAuthsMapper adminAuthsMapper;
	@Autowired
	private AdminInfoMapper adminInfoMapper;
	@Autowired
	private SysAuthsMapper sysAuthsMapper;
	@Autowired
	private RedisUtils redisUtils;


	@Value("${admin_auths_key}")
	private String ADMIN_AUTH_KEY;

	@Value("${session_key_expire_key}")
	private Long AUTO_LOGIN_SESSION_EXPIRE;

	@Override
	public AdminInfoVO verifyAdminAuthAndCache(String admin, String password, boolean auto) {
		AdminAuths auths = adminAuthsMapper.selectByAccount(admin);
		AdminInfoVO adminInfoVO = null;
		// 验证合法性
		if (auths != null
				&& MD5Utils.verifySaltMd5(password, auths.getPassword())) {
			adminInfoVO = getAdminInfoVOByAuths(auths, true);
			// 设置登录缓存
			if (auto) {
				String sessionKey = MD5Utils.getMd5(System.currentTimeMillis() + auths.getAccount());
				adminInfoVO.setSessionKey(sessionKey);
				// cache
				redisUtils.hset(ADMIN_AUTH_KEY, sessionKey, auths.getAdminId(), AUTO_LOGIN_SESSION_EXPIRE);
			}
		}
		// 返回值对象
		return adminInfoVO;
	}

	private AdminInfoVO getAdminInfoVOByAuths(AdminAuths auths, boolean setLoginDate) {
		AdminInfoVO adminInfoVO;
		if (setLoginDate) {
			Long nowLoginDate = System.currentTimeMillis();
			auths.setLastLoginDate(nowLoginDate);
		}
		SysAuths sysAuth = sysAuthsMapper.selectSysAuthByAdminId(auths.getAdminId());
		adminAuthsMapper.updateById(auths);
		adminInfoVO = new AdminInfoVO(auths.getAdminId(), auths.getAccount(),
				null, sysAuth.getId(), sysAuth.getName(),
				StringUtils.equals(auths.getPwdLastSet(), "0"), auths.getLastLoginDate(),null);
		return adminInfoVO;
	}

	public List<AdminInfoVO> listAdminInfoVOByAuths(Collection<AdminAuths> auths) {
		List<AdminInfoVO> adminInfoVOList = new ArrayList<>();
		auths.forEach(a -> adminInfoVOList.add(getAdminInfoVOByAuths(a, false)));
		return adminInfoVOList;
	}

	@Override
	public Boolean deleteAdmin(Integer id) {
		QueryWrapper wrapper = new QueryWrapper<>().eq("admin_id", id);
		return adminInfoMapper.deleteById(id) == 1 && adminAuthsMapper.delete(wrapper) == 1;
	}

	@Override
	public AdminInfo VO2AdminInfo(AdminInfoVO adminInfoVO) {
		return new AdminInfo(adminInfoVO.getAdminId(),adminInfoVO.getAccount(),adminInfoVO.getAuth());
	}

	@Override
	public AdminAuths VO2AdminAuths(AdminInfoVO adminInfoVO) {
		return new AdminAuths(null,adminInfoVO.getAccount(),MD5Utils.getSaltMd5(adminInfoVO.getPassword()),null,adminInfoVO.getAdminId(),"0");
	}

	@Transactional
	@Override
	public AdminInfoVO updatePassword(AdminInfoVO admin, String password) {
		String account = admin.getAccount();
		AdminAuths adminAuths = adminAuthsMapper.selectByAccount(account);
		try {
			adminAuths.setPassword(MD5Utils.getSaltMd5(password));
			adminAuths.setPwdLastSet("1");
			adminAuthsMapper.updateById(adminAuths);
			admin.setPwdLastSet(false);
			return admin;
		} catch (Exception e) {
			logger.debug("Update Password Exception : {}", e.toString());
			return null;
		}
	}

	@Override
	public AdminInfoVO verifySessionKey(String sessionKey) {
		Integer adminId = (Integer) redisUtils.hget(ADMIN_AUTH_KEY, sessionKey);
		AdminInfoVO adminInfoVO = null;
		if (adminId != null) {
			AdminAuths auths = adminAuthsMapper.selectByAdminId(adminId);
			if (auths != null) {
				adminInfoVO = getAdminInfoVOByAuths(auths, true);
			}
		}
		return adminInfoVO;
	}




}
