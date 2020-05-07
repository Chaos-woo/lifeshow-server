package per.chao.lifeshow.controller.admin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import per.chao.lifeshow.entity.model.JsonResult;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/5/1 19:52
 **/
@Controller
@RequestMapping("/")
public class BirthController {
	@GetMapping("birth/0506")
	public String birthSecret() {
		return "admin/birth_secret";
	}

	@GetMapping("birth")
	public String birth() {
		return "admin/birth";
	}

	@GetMapping("birth/p")
	@ResponseBody
	public JsonResult pass(String p) {
		if (StringUtils.isNotEmpty(p)) {
			return JsonResult.ok("56CB".equals(p) ? 1 : 0);
		}
		return JsonResult.ok(0);
	}
}
