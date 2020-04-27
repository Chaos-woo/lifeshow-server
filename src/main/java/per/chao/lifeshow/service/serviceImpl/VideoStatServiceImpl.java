package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.VideoStat;
import per.chao.lifeshow.mapper.VideoStatMapper;
import per.chao.lifeshow.service.IVideoStatService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/3 14:33
 **/
@Service
public class VideoStatServiceImpl extends ServiceImpl<VideoStatMapper, VideoStat> implements IVideoStatService {
}
