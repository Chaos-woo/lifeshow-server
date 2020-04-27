package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.UserFans;
import per.chao.lifeshow.mapper.UserFansMapper;
import per.chao.lifeshow.service.IUserFansService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/30 15:26
 **/
@Service
public class UserFansServiceImpl extends ServiceImpl<UserFansMapper, UserFans> implements IUserFansService {
}
