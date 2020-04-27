package per.chao.lifeshow.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/28 15:16
 **/
@Aspect
@Component
public class LogAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(* per.chao.lifeshow.controller..*.*.*(..))")
	public void log() {
	}

	@Before("log()")
	public void doBefore(JoinPoint jp) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
		String url = request.getRequestURL().toString();
		String ip = request.getRemoteAddr();
		String requestMethod = jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName();
		Object[] args = jp.getArgs();
		RequestLog requestLog = new RequestLog(url, ip, requestMethod, args);
		logger.info("Request : {} ", requestLog);
	}

	@After("log()")
	public void doAfter() {

	}

	@AfterReturning(returning = "result", pointcut = "log()")
	public void doAfterReturning(Object result) {
		logger.info("Result : {}", result);
	}

	static class RequestLog {
		private String url;
		private String ip;
		private String requestMethod;
		private Object[] args;

		RequestLog(String url, String ip, String requestMethod, Object[] args) {
			this.url = url;
			this.ip = ip;
			this.requestMethod = requestMethod;
			this.args = args;
		}

		@Override
		public String toString() {
			return "RequestLog{" +
					"url='" + url + '\'' +
					", ip='" + ip + '\'' +
					", requestMethod='" + requestMethod + '\'' +
					", args=" + Arrays.toString(args) +
					'}';
		}
	}


}
