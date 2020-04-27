package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.UserFollowers;
import per.chao.lifeshow.mapper.UserFollowersMapper;
import per.chao.lifeshow.service.IUserFollowersService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/27 15:13
 **/
@Service
public class UserFollowersServiceImpl extends ServiceImpl<UserFollowersMapper, UserFollowers> implements IUserFollowersService {
}
