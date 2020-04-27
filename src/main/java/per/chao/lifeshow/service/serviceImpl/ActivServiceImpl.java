package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.Activs;
import per.chao.lifeshow.mapper.ActivsMapper;
import per.chao.lifeshow.service.IActivService;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/4 15:14
 **/
@Service
public class ActivServiceImpl extends ServiceImpl<ActivsMapper, Activs> implements IActivService {
	@Autowired
	private ActivsMapper activsMapper;

	@Override
	public Activs selectByName(String activName) {
		return activsMapper.selectByName(activName);
	}
}
