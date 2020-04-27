package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.UserCollectedVideos;
import per.chao.lifeshow.mapper.UserCollectedVideosMapper;
import per.chao.lifeshow.service.IUserCollectedVideosService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/3 19:03
 **/
@Service
public class UserCollectedVideosServiceImpl extends ServiceImpl<UserCollectedVideosMapper, UserCollectedVideos> implements IUserCollectedVideosService {
}
