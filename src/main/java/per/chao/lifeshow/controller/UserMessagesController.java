package per.chao.lifeshow.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.entity.vo.MessagesVO;
import per.chao.lifeshow.service.IMessagesService;

import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/8 18:08
 **/
@Api("用户消息接口")
@RestController
@RequestMapping("/u/msg")
public class UserMessagesController {
	@Autowired
	private IMessagesService messagesService;

	/**
	 * 获取用户全部未读消息
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("获取用户全部未读消息")
	@GetMapping("/all")
	@ApiImplicitParam(name = "id", value = "用户id", required = true)
	public JsonResult listUserMessage(Integer id) {
		List<MessagesVO> messagesByUserId = messagesService.getMessagesByUserId(id);
		return JsonResult.ok(messagesByUserId);
	}

	/**
	 * 更新用户消息状态
	 *
	 * @param id
	 */
	@ApiOperation("更新用户消息状态")
	@ApiImplicitParam(name = "id", value = "消息id", required = true)
	@GetMapping("/update")
	public void updateUserMessageStatus(Integer id) {
		messagesService.updateMessage(id);
	}
}
