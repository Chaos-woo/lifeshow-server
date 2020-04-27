package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.UserBlacklist;
import per.chao.lifeshow.mapper.UserBlacklistMapper;
import per.chao.lifeshow.service.IUserBlacklistService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/27 17:41
 **/
@Service
public class UserBlacklistServiceImpl extends ServiceImpl<UserBlacklistMapper, UserBlacklist> implements IUserBlacklistService {
}
