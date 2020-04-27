package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import per.chao.lifeshow.entity.pojo.*;
import per.chao.lifeshow.entity.vo.UserVideoVO;
import per.chao.lifeshow.entity.vo.UserWorksVO;
import per.chao.lifeshow.entity.vo.VideoManageVO;
import per.chao.lifeshow.mapper.*;
import per.chao.lifeshow.service.IVideosService;
import per.chao.lifeshow.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/29 13:54
 **/
@Service
public class VideosServiceImpl extends ServiceImpl<VideosMapper, Videos> implements IVideosService {
	@Value("${video.path}")
	private String videoPath;
	@Value("${video.cover.path}")
	private String videoCoverPath;
	@Value("${server.url}")
	private String serverUrl;
	@Value("${video_liked_record_key}")
	private String videoLikedRecord;

	@Autowired
	private VideosMapper videosMapper;
	@Autowired
	private ActivsMapper activsMapper;
	@Autowired
	private VideoStatMapper videoStatMapper;
	@Autowired
	private DanmakuMapper danmakuMapper;
	@Autowired
	private UserCollectedVideosMapper userCollectedVideosMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserReportsMapper userReportsMapper;
	@Autowired
	private BgmsMapper bgmsMapper;
	@Autowired
	private UserStatMapper userStatMapper;
	@Autowired
	private RedisUtils redisUtils;

	private static final int RANDOM_VIDEO_COUNT = 10;

	@Override
	public Integer uploadVideo(Videos videos, String activName, MultipartFile file) {
		Activs activs = activsMapper.selectByName(activName);
		activs.setActivsCount(activs.getActivsCount() + 1);
		activsMapper.updateById(activs);
		Bgms bgm = null;
		if(videos.getBgmId() != (-1)){
			bgm = bgmsMapper.selectById(videos.getBgmId());
			bgm.setCited(bgm.getCited()+1);
			bgmsMapper.updateById(bgm);
		}else {
			videos.setBgmId(1);
		}

		videos.setActivId(activs.getId());
		videos.setStatusId(1);
		videos.setCreatedAt(System.currentTimeMillis());
		videosMapper.insert(videos);
		try {
			String path = saveFile(String.valueOf(videos.getId()), "video", file);
			//通过 ffmpeg 工具得到视频的时长、宽高信息
			Map<String, Object> videoInfo = FfmpegUtils.getVideoInfo(path);
			Double width = (Double) videoInfo.getOrDefault("width", null);
			Double height = (Double) videoInfo.getOrDefault("height", null);
			videos.setVideoWidth(width != null ? width : 0.0);
			videos.setVideoHeight(height != null ? height : 0.0);
			videos.setVideoSize(0.0);
			// 保存最终成品文件路径
			videos.setVideoPath(path);

			if (videos.getBgmId() != 1){
				// 通过 ffmpeg 裁剪短视频缓存为最终成品
				assert bgm != null;
				String finalPath = FfmpegUtils.cutAndMerge(videos,bgm);
				videos.setVideoPath(finalPath);
			}

			// 保存一个默认封面
			saveDefaultCover(videos);

			videosMapper.updateById(videos);
			VideoStat videoStat = new VideoStat(0, 0, 0, 0, videos.getId());
			videoStatMapper.insert(videoStat);

			UserStat userStat = userStatMapper.selectByUserId(videos.getCreatedBy());
			userStat.setWorksCount(userStat.getWorksCount()+1);
			userStatMapper.updateById(userStat);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return videos.getId();
	}

	private void saveDefaultCover(Videos videos) {
//		URL resource = Thread.currentThread().getContextClassLoader().getResource("cd.jpg");
		ClassPathResource resource = new ClassPathResource("cd.jpg");
		try {
//			String token = resource.getPath();
			String coverPath = videoCoverPath + videos.getId()+".jpg";
			videos.setCoverPath(coverPath);
			File file = new File(coverPath);
			FileUtils.copyInputStreamToFile(resource.getInputStream(),file);
//			FileCopyUtils.copy(token, coverPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String saveFile(String fileName, String type, MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			String filename = file.getOriginalFilename();
			String suffix = FilenameUtils.getExtension(filename);
			String finalName = fileName + "." + suffix;
			File f = new File((StringUtils.equals("video", type) ? videoPath : videoCoverPath) + finalName);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			file.transferTo(f);
			return f.getPath();
		}
		return "";
	}

	@Override
	public Boolean uploadCover(Integer videoId, MultipartFile file) {
		Videos videos = videosMapper.selectById(videoId);
		try {
			String path = saveFile(String.valueOf(videoId), "cover", file);
			videos.setCoverPath(path);
			videosMapper.updateById(videos);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Map<String, Object> selectByUserId(Integer id, Integer pages, Integer limits) {
		List<UserWorksVO> worksVOS = videosMapper.selectByUserId(id, (pages - 1) * limits, limits);
		processSearchWorksVO(worksVOS);
		Integer total = videosMapper.countByUserId(id);
		return DBPageUtils.toPage(worksVOS, (total + limits - 1) / limits + 1, total, limits, pages);
	}

	@Override
	public Map<String, Object> selectCollectedByUserId(Integer id, Integer pages, Integer limits) {
		List<UserWorksVO> worksVOS = videosMapper.selectCollectedByUserId(id, (pages - 1) * limits, limits);
		processSearchWorksVO(worksVOS);
		Integer total = videosMapper.countByUserId(id);
		return DBPageUtils.toPage(worksVOS, (total + limits - 1) / limits + 1, total, limits, pages);
	}

	@Override
	public Map<String, Object> selectByKeyword(String keyword, Integer pages, Integer limits) {
		List<UserWorksVO> worksVOS = videosMapper.selectByKeyword(keyword, (pages - 1) * limits, limits);
		processSearchWorksVO(worksVOS);
		Integer total = videosMapper.countByKeyword(keyword);
		return DBPageUtils.toPage(worksVOS, (total + limits - 1) / limits + 1, total, limits, pages);
	}

	private void processSearchWorksVO(List<UserWorksVO> worksVOS) {
		worksVOS.forEach(vo -> {
			vo.setCreatedAt(DateFormatter.to(Long.valueOf(vo.getCreatedAt())));
			File f = new File(vo.getCover());
			QueryWrapper<Danmaku> wrapper = new QueryWrapper<>();
			wrapper.eq(FieldToUnderline.to(Danmaku.Fields.videoId), vo.getId());
			vo.setCover(serverUrl + "/video/" + f.getName());
			vo.setLikedCount(NumberFormatterCustom.to(Integer.valueOf(vo.getLikedCount())));
			vo.setCommentCount(NumberFormatterCustom.to(Integer.valueOf(vo.getCommentCount())));
			vo.setSharedCount(NumberFormatterCustom.to(Integer.valueOf(vo.getSharedCount())));
			vo.setFavoredCount(NumberFormatterCustom.to(Integer.valueOf(vo.getFavoredCount())));
			vo.setDanmakuCount(NumberFormatterCustom.to(danmakuMapper.selectCount(wrapper)));
		});
	}

	@Override
	public List<UserVideoVO> randVideoSet(Integer id, Boolean attention) {
		LinkedList<UserVideoVO> videoVOSet = new LinkedList<>();
		Integer first = videosMapper.first().getId();
		Integer last = videosMapper.last().getId();
		int loopCount = 0;
		boolean attentionBackup = attention;
		while (videoVOSet.size() != RANDOM_VIDEO_COUNT) {
			int randId = RandomNumberUtils.getNumIncludeMinAndMax(first, last);
			UserVideoVO videoVO = videosMapper.selectOneVideo(randId);
			if (videoVO != null) {
				QueryWrapper<UserFollowers> wrapperFollower = new QueryWrapper<>();
				QueryWrapper<UserFollowers> isFollower = wrapperFollower.eq(FieldToUnderline.to(UserFollowers.Fields.followedUser), videoVO.getAuthorId());
				if (attentionBackup && isFollower == null) {
					loopCount++;
					if (loopCount >= 10) {
						attentionBackup = false;
					}
					continue;
				}
				processUserVideoVo(id, videoVO);
				videoVOSet.add(videoVO);
			}
		}
		return videoVOSet;
	}

	@Override
	public UserVideoVO getOneVideoDetail(Integer id, Integer videoId) {
		UserVideoVO videoVO = videosMapper.selectOneVideo(videoId);
		if (videoVO != null) {
			processUserVideoVo(id, videoVO);
			return videoVO;
		}
		return null;
	}

	@Override
	public List<UserVideoVO> randActivVideoSet(Integer id, Integer activId) {
		LinkedList<UserVideoVO> videoVOSet = new LinkedList<>();
		while (videoVOSet.size() != RANDOM_VIDEO_COUNT) {
			UserVideoVO videoVO = videosMapper.selectOneVideoByActiv(activId);
			if (videoVO != null) {
				processUserVideoVo(id, videoVO);
				videoVOSet.add(videoVO);
			}
		}
		return videoVOSet;
	}

	private void processUserVideoVo(Integer id, UserVideoVO videoVO) {
		String key = videoLikedRecord + videoVO.getVideoId();
		File coverFile = new File(videoVO.getCover());
		videoVO.setCover(serverUrl + "/video/" + coverFile.getName());
		File videoFile = new File(videoVO.getVideoSrc());
		videoVO.setVideoSrc(serverUrl + "/video/play/" + videoFile.getName());
		videoVO.setLikedCount(NumberFormatterCustom.to(Integer.valueOf(videoVO.getLikedCount())));
		videoVO.setCommentCount(NumberFormatterCustom.to(Integer.valueOf(videoVO.getCommentCount())));
		videoVO.setSharedCount(NumberFormatterCustom.to(Integer.valueOf(videoVO.getSharedCount())));
		videoVO.setFavoredCount(NumberFormatterCustom.to(Integer.valueOf(videoVO.getFavoredCount())));
		videoVO.setIsLiked(redisUtils.getBit(key, id));
		LambdaQueryWrapper<UserCollectedVideos> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserCollectedVideos::getUserId, id).eq(UserCollectedVideos::getVideoId, videoVO.getVideoId());
		videoVO.setIsCollected(userCollectedVideosMapper.selectOne(wrapper) != null);
		Map<String, Object> external = new HashMap<>();
		external.put("danmaku", danmakuMapper.selectByVideoId(videoVO.getVideoId()));
		videoVO.setExternal(external);
	}

	@Override
	public void handleLiked(Integer videoId, Integer id, Boolean isLiked) {
		String key = videoLikedRecord + videoId;
		QueryWrapper<VideoStat> wrapperStat = new QueryWrapper<>();
		wrapperStat.eq(FieldToUnderline.to(VideoStat.Fields.videoId), videoId);
		VideoStat videoStat = videoStatMapper.selectOne(wrapperStat);
		if (isLiked) {
			redisUtils.setBit(key, id, false);

			videoStat.setLikedCount(videoStat.getLikedCount() - 1);
		} else {
			redisUtils.setBit(key, id, true);

			videoStat.setLikedCount(videoStat.getLikedCount() + 1);
		}
		videoStatMapper.updateById(videoStat);
	}

	@Override
	public void handleCollected(Integer videoId, Integer id, Boolean isCollected) {
		QueryWrapper<VideoStat> wrapperStat = new QueryWrapper<>();
		wrapperStat.eq(FieldToUnderline.to(VideoStat.Fields.videoId), videoId);
		VideoStat videoStat = videoStatMapper.selectOne(wrapperStat);
		if (isCollected) {
			LambdaQueryWrapper<UserCollectedVideos> wrapper = Wrappers.lambdaQuery();
			wrapper.eq(UserCollectedVideos::getUserId, id).eq(UserCollectedVideos::getVideoId, videoId);
			userCollectedVideosMapper.delete(wrapper);
			videoStat.setFavoredCount(videoStat.getFavoredCount() - 1);
		} else {
			UserCollectedVideos uck = new UserCollectedVideos();
			uck.setUserId(id);
			uck.setVideoId(videoId);
			userCollectedVideosMapper.insert(uck);
			videoStat.setFavoredCount(videoStat.getFavoredCount() + 1);
		}
		videoStatMapper.updateById(videoStat);
	}

	@Override
	public List<VideoManageVO> listVideoManageVO(Collection<Videos> videos, Boolean external) {
		List<VideoManageVO> videoManageVoList = new ArrayList<>();
		videos.forEach(v -> {
			VideoManageVO vo = getVideoManageVO(v);
			if (external) {
				addExternalData(vo);
			}
			videoManageVoList.add(vo);
		});
		return videoManageVoList;
	}

	private void addExternalData(VideoManageVO vo) {
		QueryWrapper<UserReports> wrapper = new QueryWrapper<>();
		wrapper.eq(FieldToUnderline.to(UserReports.Fields.videoId), vo.getId());
		List<UserReports> userReports = userReportsMapper.selectList(wrapper);
		StringBuilder content = new StringBuilder();
		userReports.forEach(r -> {
			content.append(r.getReportContent()).append(";");
		});
		Map<String, Object> external = vo.getExternal();
		external.put("content", content.toString());
		vo.setExternal(external);
	}

	private VideoManageVO getVideoManageVO(Videos v) {
		QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
		userWrapper.eq(FieldToUnderline.to(UserInfo.Fields.id), v.getCreatedBy());
		QueryWrapper<VideoStat> statWrapper = new QueryWrapper<>();
		statWrapper.eq(FieldToUnderline.to(VideoStat.Fields.videoId), v.getId());
		UserInfo userInfo = userInfoMapper.selectOne(userWrapper);
		VideoStat videoStat = videoStatMapper.selectOne(statWrapper);
		File cover = new File(v.getCoverPath());
		String coverPath = serverUrl + "/video/" + cover.getName();
		File src = new File(v.getVideoPath());
		String videoSrc = serverUrl + "/video/play/" + src.getName();
		StringBuilder stat = new StringBuilder();
		stat.append(NumberFormatterCustom.to(videoStat.getLikedCount())).append("/")
				.append(NumberFormatterCustom.to(videoStat.getFavoredCount())).append("/")
				.append(NumberFormatterCustom.to(videoStat.getCommentCount()));
		return new VideoManageVO(v.getId(), v.getVideoTitle(), coverPath, videoSrc, activsMapper.selectById(v.getActivId()).getActivName(), v.getVideoDesc(), v.getCreatedAt(), v.getBgmId(), v.getVideoSize(), userInfo.getNickname(), v.getStatusId(), stat.toString(), new HashMap<>());
	}
}
