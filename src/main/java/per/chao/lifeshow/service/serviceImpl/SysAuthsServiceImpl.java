package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.SysAuths;
import per.chao.lifeshow.mapper.SysAuthsMapper;
import per.chao.lifeshow.service.ISysAuthsService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/6 18:03
 **/
@Service
public class SysAuthsServiceImpl extends ServiceImpl<SysAuthsMapper, SysAuths> implements ISysAuthsService {
}
