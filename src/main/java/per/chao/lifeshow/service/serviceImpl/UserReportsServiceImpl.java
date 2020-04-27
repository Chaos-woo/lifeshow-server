package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.UserReports;
import per.chao.lifeshow.mapper.UserReportsMapper;
import per.chao.lifeshow.service.IUserReportsService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/3 21:18
 **/
@Service
public class UserReportsServiceImpl extends ServiceImpl<UserReportsMapper, UserReports> implements IUserReportsService {
}
