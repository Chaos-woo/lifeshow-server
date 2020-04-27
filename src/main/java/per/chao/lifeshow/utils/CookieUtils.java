package per.chao.lifeshow.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/4 11:16
 **/
public class CookieUtils {

	public static void setCookie(HttpServletResponse response, String setName, Object value, Integer expire, boolean httpOnly, String setPath) {
		Cookie cookie = new Cookie(setName, value.toString());
		cookie.setMaxAge(expire);
		cookie.setPath(setPath);
		cookie.setHttpOnly(httpOnly);
		response.addCookie(cookie);
	}

	public static void clearCookie(HttpServletResponse response, String setName, String setPath) {
		Cookie c = new Cookie(setName, null);
		c.setMaxAge(0);
		c.setPath(setPath);
		response.addCookie(c);
	}
}
