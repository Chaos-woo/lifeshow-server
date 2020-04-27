package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.Bgms;
import per.chao.lifeshow.entity.pojo.UserInfo;
import per.chao.lifeshow.mapper.BgmsMapper;
import per.chao.lifeshow.mapper.StatMapper;
import per.chao.lifeshow.mapper.UserInfoMapper;
import per.chao.lifeshow.mapper.VideosMapper;
import per.chao.lifeshow.service.IStatService;
import per.chao.lifeshow.utils.DateObtainUtils;
import per.chao.lifeshow.utils.RedisUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/26 23:58
 **/
@Service
public class StatServiceImpl implements IStatService {
	@Autowired
	private BgmsMapper bgmsMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private VideosMapper videosMapper;
	@Autowired
	private StatMapper statMapper;
	@Autowired
	private RedisUtils redisUtils;

	@Value("${minip.access}")
	private String miniProgramAccess;
	@Value("${minip.access.now}")
	private String miniProgramAccessNow;

	@Override
	public List<LinkedList<String>> music() {
		LinkedList<LinkedList<String>> result = new LinkedList<>();
		LinkedList<String> type = new LinkedList<>();
		LinkedList<String> data = new LinkedList<>();
		QueryWrapper<Bgms> wrapper;
		// 添加类型
		type.addLast("总数");
		type.addLast("已裁剪");
		type.addLast("未裁剪");
		result.addLast(type);
		// 添加数据
		data.addLast(bgmsMapper.selectCount(null).toString());
		wrapper = new QueryWrapper<>();
		wrapper.eq(Bgms.Fields.status, 1);
		data.addLast(bgmsMapper.selectCount(wrapper).toString());
		wrapper = new QueryWrapper<>();
		wrapper.eq(Bgms.Fields.status, 0);
		data.addLast(bgmsMapper.selectCount(wrapper).toString());
		result.addLast(data);
		return result;
	}

	@Override
	public List<LinkedList<String>> users() {
		LinkedList<LinkedList<String>> result = new LinkedList<>();
		LinkedList<String> type = new LinkedList<>();
		LinkedList<String> data = new LinkedList<>();
		QueryWrapper<UserInfo> wrapper;
		// 添加类型
		type.addLast("总数");
		type.addLast("未知");
		type.addLast("男");
		type.addLast("女");
		result.addLast(type);
		data.addLast(userInfoMapper.selectCount(null).toString());
		wrapper = new QueryWrapper<>();
		wrapper.eq(UserInfo.Fields.gender, 0);
		data.addLast(userInfoMapper.selectCount(wrapper).toString());
		wrapper = new QueryWrapper<>();
		wrapper.eq(UserInfo.Fields.gender, 1);
		data.addLast(userInfoMapper.selectCount(wrapper).toString());
		wrapper = new QueryWrapper<>();
		wrapper.eq(UserInfo.Fields.gender, 2);
		data.addLast(userInfoMapper.selectCount(wrapper).toString());
		result.addLast(data);
		return result;
	}

	@Override
	public List<LinkedList<String>> videos() {
		LinkedList<LinkedList<String>> result = new LinkedList<>();
		LinkedList<String> type = new LinkedList<>();
		LinkedList<String> data = new LinkedList<>();
		type.addLast("总数");
		data.addLast(videosMapper.selectCount(null).toString());
		type.addLast("日期");
		data.addLast("每日增长数");
		process7DaysVideosDataStat(type, data);
		result.addLast(type);
		result.addLast(data);
		return result;
	}

	private void process7DaysVideosDataStat(LinkedList<String> type, LinkedList<String> data) {
		// -6 -5 -4 -3 -2 -1 0
		for (int i = (-6); i <= 0; i++) {
			if (i == 0) {
				type.addLast(DateObtainUtils.obtainDateString(i) + "[今日]");
			} else {
				type.addLast(DateObtainUtils.obtainDateString(i));
			}
			data.addLast(statMapper.countVideosBetweenDate(DateObtainUtils.obtainMillWithZero(i - 1), DateObtainUtils.obtainMillWithZero(i)).toString());
		}
	}

	@Override
	public List<LinkedList<String>> user() {
		LinkedList<LinkedList<String>> result = new LinkedList<>();
		LinkedList<String> type = new LinkedList<>();
		LinkedList<String> data = new LinkedList<>();
		type.addLast("日期");
		data.addLast("每日新增用户");
		for (int i = (-6); i <= 0; i++) {
			if (i == 0) {
				type.addLast(DateObtainUtils.obtainDateString(i) + "[今日]");
			} else {
				type.addLast(DateObtainUtils.obtainDateString(i));
			}
			data.addLast(statMapper.countUsersBetweenDate(DateObtainUtils.obtainMillWithZero(i - 1), DateObtainUtils.obtainMillWithZero(i)).toString());
		}
		result.addLast(type);
		result.addLast(data);
		return result;
	}

	@Override
	public List<LinkedList<String>> video() {
		LinkedList<LinkedList<String>> result = new LinkedList<>();
		LinkedList<String> type = new LinkedList<>();
		LinkedList<String> data = new LinkedList<>();
		type.addLast("日期");
		data.addLast("每日新增短视频");
		process7DaysVideosDataStat(type, data);
		result.addLast(type);
		result.addLast(data);
		return result;
	}

	@Override
	public void incrAccess(long value) {
		redisUtils.incr(miniProgramAccess,value);
		redisUtils.incr(miniProgramAccessNow,value);
	}
}
