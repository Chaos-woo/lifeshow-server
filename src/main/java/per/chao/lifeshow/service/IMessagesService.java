package per.chao.lifeshow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import per.chao.lifeshow.entity.pojo.Messages;
import per.chao.lifeshow.entity.vo.MessagesVO;

import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/8 17:42
 **/
public interface IMessagesService extends IService<Messages> {
	void saveProhibitionMessage(String videoTitle,Integer id);

	List<MessagesVO> getMessagesByUserId(Integer id);

	void updateMessage(Integer id);
}
