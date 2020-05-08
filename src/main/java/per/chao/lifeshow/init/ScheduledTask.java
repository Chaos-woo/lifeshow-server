package per.chao.lifeshow.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import per.chao.lifeshow.entity.pojo.UserInfo;
import per.chao.lifeshow.entity.pojo.UserStat;
import per.chao.lifeshow.mapper.UserInfoMapper;
import per.chao.lifeshow.mapper.UserStatMapper;
import per.chao.lifeshow.mapper.VideosMapper;
import per.chao.lifeshow.utils.RedisUtils;

import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/5/8 11:04
 **/
@Component
public class ScheduledTask {
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private UserStatMapper userStatMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private VideosMapper videosMapper;


	@Value("${minip.access.now}")
	private String miniProgramAccessNow;
	@Value("${video_liked_record_key}")
	private String videoLikedRecord;

	@Scheduled(cron = "0 0 0 * * ?")
	public void accessNumDailyToZero() {
		redisUtils.set(miniProgramAccessNow, 0);
	}

	@Scheduled(cron = "10 0 0 * * ?")
	public void statisticsTotalLikedWithUser() {
		Integer startId = userInfoMapper.first();
		Integer lastId = userInfoMapper.last();
		for (int i = startId; i <= lastId; i++) {
			Long totalLiked = 0L;
			UserInfo u = userInfoMapper.selectById(i);
			if (u != null) {
				List<Integer> videoIds = videosMapper.selectIdsByUserId(u.getId());
				for (Integer id : videoIds) {
					totalLiked += redisUtils.bitCount(videoLikedRecord + id);
				}
				UserStat us = userStatMapper.selectByUserId(u.getId());
				us.setReceivedLikedCount(totalLiked);
				userStatMapper.updateById(us);
			}
		}
	}
}
