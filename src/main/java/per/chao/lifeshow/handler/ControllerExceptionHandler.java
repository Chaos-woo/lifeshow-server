package per.chao.lifeshow.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import per.chao.lifeshow.entity.model.JsonResult;
import per.chao.lifeshow.exception.DataNotExistException;
import per.chao.lifeshow.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/28 22:44
 **/
@ControllerAdvice
public class ControllerExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//	/**
//	 * 处理全部异常
//	 *
//	 * @param e
//	 * @return
//	 */
//	@ExceptionHandler
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	public JsonResult handleException(Exception e) {
//		logger.error("Exception : {}", Arrays.toString(e.getStackTrace()));
//		return new JsonResult(false, "请求失败");
//	}

	/**
	 * 处理NotFoundException异常，处理页面
	 *
	 * @param request
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler({NotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView notFoundExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
		StringBuffer requestURL = request.getRequestURL();
		logger.error("NotFoundException url : {}; Exception : {}", request, e);

		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}

		ModelAndView nav = new ModelAndView();
		nav.addObject("url", requestURL);
		nav.addObject("exception", e);
		nav.setViewName("error/error");
		return nav;
	}

	/**
	 * 处理DataNotFoundException异常，返回json数据
	 *
	 * @param request
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(DataNotExistException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public JsonResult dataNotFoundExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
		StringBuffer requestUrl = request.getRequestURL();
		logger.error("DataNotFoundException url : {}; Exception : {}", request, e);

		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}

		return new JsonResult(true, "data not found；request url : " + requestUrl, null);
	}
}
