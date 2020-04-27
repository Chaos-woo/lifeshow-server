package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.AdminInfo;
import per.chao.lifeshow.mapper.AdminInfoMapper;
import per.chao.lifeshow.service.IAdminInfoService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/6 16:22
 **/
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo> implements IAdminInfoService {
}
