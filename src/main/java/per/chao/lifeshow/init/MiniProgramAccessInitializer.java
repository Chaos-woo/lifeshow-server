package per.chao.lifeshow.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import per.chao.lifeshow.utils.RedisUtils;

import javax.annotation.PostConstruct;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/26 23:47
 **/
@Component
public class MiniProgramAccessInitializer {
	@Autowired
	private RedisUtils redisUtils;

	@Value("${minip.access}")
	private String miniProgramAccess;
	@Value("${minip.access.now}")
	private String miniProgramAccessNow;

	@PostConstruct
	public void init(){
		if (!redisUtils.hasKey(miniProgramAccess)){
			redisUtils.set(miniProgramAccess,0);
		}
		if (!redisUtils.hasKey(miniProgramAccessNow)){
			redisUtils.set(miniProgramAccessNow,0);
		}
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void everydayAccessScheduledTask(){
		redisUtils.set(miniProgramAccessNow,0);
	}

}
