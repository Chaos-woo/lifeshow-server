package per.chao.lifeshow.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import per.chao.lifeshow.entity.vo.AdminInfoVO;
import per.chao.lifeshow.service.IAdminService;
import per.chao.lifeshow.utils.CookieUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/3 16:53
 **/
public class AdminLoginInterceptor extends WebContentInterceptor {
	@Autowired
	private IAdminService adminService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			Cookie[] cookies = request.getCookies();
			if (cookies == null) {
				try {
					response.sendRedirect("/");
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
			for (Cookie cookie : cookies) {
				if ("auto".equals(cookie.getName())) {
					String sessionKey = cookie.getValue();
					AdminInfoVO adminInfoVO = adminService.verifySessionKey(sessionKey);
					if (adminInfoVO != null) {
						try {
							response.sendRedirect("/admin/dashboard");
						} catch (IOException e) {
							e.printStackTrace();
						}
						session.setAttribute("admin", adminInfoVO);
						return true;
					} else {
						CookieUtils.clearCookie(response, "auto", "/");
						return true;
					}
				}
			}
		}
		return true;
	}
}
