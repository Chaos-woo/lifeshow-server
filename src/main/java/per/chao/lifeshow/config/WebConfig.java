package per.chao.lifeshow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import per.chao.lifeshow.interceptor.AdminLoginInterceptor;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/29 11:28
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${bgm.path}")
	private String bgmPath;
	@Value("${bgm.relative.path}")
	private String bgmRelativePath;
	@Value("${bgm.cover.path}")
	private String bgmCoverPath;
	@Value("${bgm.cover.relative.path}")
	private String bgmRelativeCoverPath;
	@Value("${video.path}")
	private String videoPath;
	@Value("${video.relative.path}")
	private String videoRelativePath;
	@Value("${video.cover.path}")
	private String videoCoverPath;
	@Value("${video.cover.relative.path}")
	private String videoRelativeCoverPath;

	// 添加资源映射
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/**");
		registry.addResourceHandler(bgmRelativePath).addResourceLocations("file:" + bgmPath);
		registry.addResourceHandler(bgmRelativeCoverPath).addResourceLocations("file:" + bgmCoverPath);
		registry.addResourceHandler(videoRelativePath).addResourceLocations("file:" + videoPath);
		registry.addResourceHandler(videoRelativeCoverPath).addResourceLocations("file:" + videoCoverPath);
	}

	@Bean
	public AdminLoginInterceptor adminLoginInterceptor() {
		return new AdminLoginInterceptor();
	}

	// 添加登录拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminLoginInterceptor())
				.addPathPatterns("/admin")
				.addPathPatterns("/admin/**");
	}

	// 添加跨域支持
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("*")
				.allowedOrigins("*")
				.allowedHeaders("*");
	}
}
