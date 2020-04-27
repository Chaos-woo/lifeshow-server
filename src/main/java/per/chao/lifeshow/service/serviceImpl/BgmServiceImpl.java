package per.chao.lifeshow.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import per.chao.lifeshow.entity.pojo.Bgms;
import per.chao.lifeshow.entity.vo.BgmsVO;
import per.chao.lifeshow.mapper.BgmsMapper;
import per.chao.lifeshow.service.IBgmService;
import per.chao.lifeshow.utils.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/7 16:50
 **/
@Service
public class BgmServiceImpl extends ServiceImpl<BgmsMapper, Bgms> implements IBgmService {
	@Autowired
	private BgmsMapper bgmsMapper;

//	@Value("${qq.music.search}")
//	private String SEARCH_URL;
//	@Value("${qq.music.pic}")
//	private String PIC_URL;
//	@Value("${qq.music.download}")
//	private String DOWNLOAD_URL;
//	@Value("${qq.music.songList}")
//	private String SONG_LIST_URL;
//	@Value("${qq.music.hotSongList}")
//	private String HOT_SONG_LIST_URL;
//	@Value("${qq.music.topList}")
//	private String TOP_LIST_URL;

	@Value("${rand.song.url}")
	private String randSongUrl;
	@Value("${keyword.song.url}")
	private String keywordSongUrl;
	@Value("${cover.url.prefix}")
	private String coverUrlPrefix;
	@Value("${current.qq.music.addr}")
	private String currentQQMusicAddr;

	@Value("${bgm.path}")
	private String bgmPath;
	@Value("${bgm.cover.path}")
	private String bgmCoverPath;
	@Value("${server.url}")
	private String serverUrl;

	/**
	 * 根据关键字查询歌曲		// 注意：翻页0和1返回的相同数据
	 *
	 * @param pages
	 * @param keyword
	 * @return
	 */
	public Map<String, Object> listByKeyword(Integer pages, String keyword) {
		// get请求的参数设置
		Map<String, String> param = new HashMap<>();
		param.put("aggr", "1");
		param.put("cr", "1");
		param.put("flag_qc", "0");
		param.put("p", String.valueOf(pages));
		param.put("n", "10");
		param.put("w", keyword);
		// get请求数据
		String callbackJson = HttpClientUtils.doGet(keywordSongUrl, param);
		String json = callbackJson.substring(9, callbackJson.length() - 1);
		// 解析数据
		JsonNode songs = null;
		try {
			JsonNode all = JsonUtils.MAPPER.readTree(json);
			JsonNode data = JsonUtils.MAPPER.readTree(all.get("data").toString());
			songs = JsonUtils.MAPPER.readTree(data.get("song").toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		// 获取结点数据
		assert songs != null;
		List<BgmsVO> bgmsVOS = new ArrayList<>();
		JsonNode songList = songs.get("list");
		songList.forEach(song -> {
			BgmsVO vo = new BgmsVO();
			vo.setSongName(song.get("songname").asText());
			vo.setSongMid(song.get("songmid").asText());
			vo.setAlbumid(song.get("albumid").asText());
			// set singer
			List<Object> singer = new ArrayList<>();
			JsonNode singerArray = song.get("singer");
			singerArray.forEach(s -> {
				Map<String, String> singerMap = new HashMap<>();
				singerMap.put("name", s.get("name").asText());
				singer.add(singerMap);
			});
			vo.setSinger(singer);
			bgmsVOS.add(vo);

		});
//		// 返回page数据
		return NetBgmPageUtils.getPage(songs.get("totalnum").asInt(), songs.get("curnum").asInt(), songs.get("curpage").asInt(), songs.get("totalnum").asInt(), bgmsVOS);
	}

	private List<BgmsVO> getBgmsVOS(JsonNode songList) {
		List<BgmsVO> bgmsVOS = new ArrayList<>();
		songList.forEach(song -> {
			JsonNode node = song.get("data");
			if (bgmsVOS.size() < 50) {
				BgmsVO vo = new BgmsVO();
				vo.setSongName(node.get("songname").asText());
				vo.setSongMid(node.get("songmid").asText());
				vo.setAlbumid(node.get("albumid").asText());
				// set singer
				List<Object> singer = new ArrayList<>();
				JsonNode singerArray = node.get("singer");
				singerArray.forEach(s -> {
					Map<String, String> singerMap = new HashMap<>();
					singerMap.put("name", s.get("name").asText());
					singer.add(singerMap);
				});
				vo.setSinger(singer);
				bgmsVOS.add(vo);
			}
		});
		return bgmsVOS;
	}

	private Map<String, Object> getSongListPage(String url, Map<String, String> param) {
		// get请求
		String json = HttpClientUtils.doGet(url, param);
		//解析数据
		JsonNode songList = null;
		try {
			JsonNode node = JsonUtils.MAPPER.readTree(json);
			songList = JsonUtils.MAPPER.readTree(node.get("songlist").toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		assert songList != null;
		List<BgmsVO> bgmsVOS = getBgmsVOS(songList);
		return NetBgmPageUtils.getPage(0, bgmsVOS.size(), 1, 0, bgmsVOS);
	}


//	public Map<String, Object> getTopSongList() {
//		// get参数设置
//		Map<String, String> param = new HashMap<>();
//		param.put("id", String.valueOf(60));
//		param.put("pageSize", "30");
//		param.put("page", "1");
//		param.put("format", "1");
//		return getSongListPage(TOP_LIST_URL, param);
//	}

//	private String getAndProcessSongList(Integer id) {
//		int pages = (int) (Math.random() * 5) + 1;
//		int randListIndex = (int) (Math.random() * 60) + 1;
//		// get参数设置
//		Map<String, String> param = new HashMap<>();
//		param.put("categoryId", String.valueOf(id));
//		param.put("sortId", "3");
//		param.put("pageSize", "60");
//		param.put("page", String.valueOf(pages));
//		// get请求
//		String json = HttpClientUtils.doGet(HOT_SONG_LIST_URL, param);
//		// 解析数据
//		JsonNode list = null;
//		try {
//			JsonNode node = JsonUtils.MAPPER.readTree(json);
//			JsonNode data = JsonUtils.MAPPER.readTree(node.get("data").toString());
//			list = JsonUtils.MAPPER.readTree(data.get("list").toString());
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		assert list != null;
//		JsonNode randList = list.get(randListIndex);
//		return randList.get("dissid").asText();
//	}

	private Map<String, Object> songList(String id) {
		// get参数设置
		Map<String, String> param = new HashMap<>();
		param.put("g_tk", "5381");
		param.put("uin", "0");
		param.put("format", "json");
		param.put("inCharset", "utf-8");
		param.put("outCharset", "utf-8¬ice=0");
		param.put("platform", "h5");
		param.put("needNewCode", "1");
		param.put("tpl", "3");
		param.put("page", "detail");
		param.put("type", "top");
		param.put("topid", id);
		param.put("_", "1520777874472");
		return getSongListPage(randSongUrl, param);
	}

//	@Override
//	public Map<String, Object> getRandSongList(Integer id) {
//		return songList(String.valueOf(getAndProcessSongList(id)));
//	}

	@Override
	public Map<String, Object> getRandSongList() {
		return songList(String.valueOf(36));
	}

	@Override
	public Bgms download(Bgms bgms) {
		bgmsMapper.insert(bgms);
		QueryWrapper<Bgms> wrapper = new QueryWrapper<>();
		wrapper.eq("mid", bgms.getMid());
		Bgms b = bgmsMapper.selectOne(wrapper);
		if (downloadBgm(b) && downloadCover(b)) {//
//			Long duration = MP3Utils.getMp3FileDuration(b.getBgmPath());
			Long duration = M4aUtils.getDuration(b.getBgmPath());
			b.setBgmSeconds(duration);
			bgmsMapper.updateById(b);
			return b;
		}
		bgmsMapper.deleteById(b.getId());
		return null;
	}

	private String getRealBgmDownloadPath(String mid) throws JsonProcessingException {
		JsonNode realPathPrefixNode = JsonUtils.MAPPER.readTree(HttpClientUtils.doGet(currentQQMusicAddr));
		String realPathPrefix = realPathPrefixNode.get("req").get("data").get("freeflowsip").get(0).asText();
		String vKey = getVKey(mid);
		return realPathPrefix + "C400" + mid + ".m4a?&vkey=" + vKey + "&fromtag=0&guid=000000000";
	}

	private String getVKey(String mid) throws JsonProcessingException {
		String url = "https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?format=json205361747&platform=yqq&cid=205361747&songmid=" + mid + "&filename=C400" + mid + ".m4a&guid=000000000";
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonNode data = JsonUtils.MAPPER.readTree(String.valueOf(json));
		return data.get("data").get("items").get(0).get("vkey").asText();
	}

	public static void main(String[] args) {
//		try {
//			System.out.println(new BgmServiceImpl().getRealBgmDownloadPath("000jf57r1hnHtK"));
//			;
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		new BgmServiceImpl().downloadCover(5);

//		String url = COVER_URL_PREFIX + 1570600 + "_0.jpg";
//		String s = HttpClientUtils.getRedirectInfo(url);
//		System.out.println(s);
	}

	private Boolean downloadBgm(Bgms bgms) {
		String mid = bgms.getMid();
		Integer id = bgms.getId();
		String downloadUrl = null;
		try {
			downloadUrl = getRealBgmDownloadPath(mid);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		}
		String path = bgmPath + id + ".m4a";
		try {
			NetDownloadUtils.download(downloadUrl, path);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		bgms.setBgmPath(path);
		bgms.setCited(0);
		bgms.setStatus(0);
		return true;
	}

	private Boolean downloadCover(Bgms bgms) {
		Integer id = bgms.getId();
		String path = bgmCoverPath + id + ".jpg";
		if ("0".equals(bgms.getAlbumid())) {
//			URL resource = Thread.currentThread().getContextClassLoader().getResource("cd.jpg");
			ClassPathResource resource = new ClassPathResource("cd.jpg");
			try {
				File file = new File(path);
				FileUtils.copyInputStreamToFile(resource.getInputStream(),file);
//				String token = resource.getPath();
//				FileCopyUtils.copy(token, path);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			String url = coverUrlPrefix + bgms.getAlbumid() + "_0.jpg";
			String downloadUrl = HttpClientUtils.getRedirectInfo(url);
			try {
				NetDownloadUtils.download(downloadUrl, path);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		bgms.setBgmCover(path);
		return true;
	}

//	private Boolean downloadBgm(Bgms bgms) {
//		String mid = bgms.getMid();
//		Integer id = bgms.getId();
//		String url = DOWNLOAD_URL + "?id=" + mid + "&isRedirect=0&quality=128";
//		String json = HttpClientUtils.doGet(url);
//		String downloadUrl;
//		try {
//			JsonNode node = JsonUtils.MAPPER.readTree(json);
//			downloadUrl = node.get("data").asText();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//			return false;
//		}
//		String path = bgmPath + id + ".mp3";
//		try {
//			NetDownloadUtils.download(downloadUrl, path);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		bgms.setBgmPath(path);
//		return true;
//	}
//
//	private Boolean downloadCover(Bgms bgms) {
//		Integer id = bgms.getId();
//		String path = bgmCoverPath + id + ".jpg";
//		if ("0".equals(bgms.getAlbumid())) {
//			String token = getClass().getResource("cd.png").getFile();
//			try {
//				copyBgmCover(token,path);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} else {
//			String url = COVER_URL_PREFIX + bgms.getAlbumid() + "_0.jpg";
//			try {
//				NetDownloadUtils.download(url, path);
//			} catch (Exception e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//		bgms.setBgmCover(path);
//		return true;
//	}

	// =========================================================================
	@Override
	public Boolean deleteBgmById(Integer id) {
		Bgms b = bgmsMapper.selectById(id);
		if (b == null) {
			return false;
		}
		File audioFile = new File(b.getBgmPath());
		File coverFile = new File(b.getBgmCover());
		return audioFile.delete() && coverFile.delete() && bgmsMapper.deleteById(id) > 0;
	}

	@Override
	public Page<Bgms> processDataWithCover(Page<Bgms> page) {
		page.getRecords().forEach(b -> {
			File cover = new File(b.getBgmCover());
			b.setBgmCover(serverUrl + "/bgm/" + cover.getName());
			File audio = new File(b.getBgmPath());
			b.setBgmPath(serverUrl + "/bgm/play/" + audio.getName());
		});
		return page;
	}

	@Override
	public String getSongPlaySrc(String mid) {
		String playSrc = null;
		try {
			playSrc = getRealBgmDownloadPath(mid);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return playSrc;
	}

	@Override
	public void bgmCutting(Integer start, Integer end, Integer id) {
		Bgms b = bgmsMapper.selectById(id);
		String newPath = FfmpegUtils.cutAudio(start, end, b.getBgmPath(), id, "-cutting.m4a");
		b.setBgmPath(newPath);
		b.setBgmSeconds((long) ((end - start) * 1000));
		b.setStatus(1);
		bgmsMapper.updateById(b);
	}

//	@Override
//	public String getSongPlaySrc(String mid) {
//		String url = DOWNLOAD_URL + "?id=" + mid + "&isRedirect=0&quality=128";
//		String json = HttpClientUtils.doGet(url);
//		JsonNode playUrl;
//		try {
//			JsonNode node = JsonUtils.MAPPER.readTree(json);
//			playUrl = node.get("data");
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//			return "";
//		}
//		return playUrl.get(0).asText();
//	}

}
