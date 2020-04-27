package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.Messages;
import per.chao.lifeshow.entity.vo.MessagesVO;
import per.chao.lifeshow.mapper.MessageTypeMapper;
import per.chao.lifeshow.mapper.MessagesMapper;
import per.chao.lifeshow.service.IMessagesService;
import per.chao.lifeshow.utils.DateFormatter;
import per.chao.lifeshow.utils.FieldToUnderline;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/8 17:42
 **/
@Service
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages> implements IMessagesService {
	@Autowired
	private MessagesMapper messagesMapper;
	@Autowired
	private MessageTypeMapper messageTypeMapper;

	public void saveProhibitionMessage(String videoTitle,Integer id){
		Messages m = new Messages();
		m.setTitle("短视频封禁");
		m.setContent("您的短视频【"+videoTitle+"】因被举报违规，现已被管理员封禁");
		m.setCreatedAt(System.currentTimeMillis());
		m.setCreatedBy("系统管理员");
		m.setExtraLink(null);
		m.setTypeId(1);
		m.setStatus("1");
		m.setUserId(id);
		messagesMapper.insert(m);
	}

	public List<MessagesVO> getMessagesByUserId(Integer id){
		LambdaQueryWrapper<Messages> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(Messages::getUserId,id).eq(Messages::getStatus,1);
		List<Messages> messages = messagesMapper.selectList(wrapper);
		List<MessagesVO> messagesVOS = new ArrayList<>();
		messages.forEach(m->{
			MessagesVO mVO = new MessagesVO();
			mVO.setId(m.getId());
			mVO.setContent(m.getContent());
			mVO.setCreatedAt(DateFormatter.to(m.getCreatedAt()));
			mVO.setExtraLink(null);
			mVO.setTitle(m.getTitle());
			mVO.setTypeId(m.getTypeId());
			messagesVOS.add(mVO);
		});
		return messagesVOS;
	}

	public void updateMessage(Integer id){
		QueryWrapper<Messages> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(Messages.Fields.id),id);
		Messages messages = messagesMapper.selectOne(wrapper);
		messages.setStatus("0");
		messagesMapper.updateById(messages);
	}
}
