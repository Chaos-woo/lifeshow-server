package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.AdminAuths;
import per.chao.lifeshow.mapper.AdminAuthsMapper;
import per.chao.lifeshow.service.IAdminAuthService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/6 10:06
 **/
@Service
public class AdminAuthsServiceImpl extends ServiceImpl<AdminAuthsMapper, AdminAuths> implements IAdminAuthService {
}
