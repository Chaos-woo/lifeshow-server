package per.chao.lifeshow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.Activs;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/4 11:56
 **/
@Service
public interface IActivService extends IService<Activs> {
	Activs selectByName(String activName);
}
