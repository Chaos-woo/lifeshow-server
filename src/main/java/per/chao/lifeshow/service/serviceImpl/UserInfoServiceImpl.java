package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.UserInfo;
import per.chao.lifeshow.mapper.UserInfoMapper;
import per.chao.lifeshow.service.IUserInfoService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/28 10:19
 **/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
}
