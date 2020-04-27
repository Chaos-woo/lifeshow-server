package per.chao.lifeshow.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import per.chao.lifeshow.entity.pojo.Bgms;
import per.chao.lifeshow.entity.pojo.Videos;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/5 21:41
 **/
@Component
public class FfmpegUtils {
	private static String FFMPEG_TOOL_PATH;
	private static final boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");

	@Value("${ffmpeg.tool.path}")
	public void setFfmpegToolPath(String ffmpegToolPath){
		FFMPEG_TOOL_PATH = ffmpegToolPath;
	}

	public static Map<String, Object> getVideoInfo(String filePath) {
		Map<String, Object> info = new HashMap<>();
		List<String> command = new ArrayList<>();
		command.add(FFMPEG_TOOL_PATH);
		command.add("-i");
		command.add(filePath);

		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			Process p = builder.start();

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			p.waitFor();
			br.close();

			String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
			Pattern pattern = Pattern.compile(regexDuration);
			Matcher m = pattern.matcher(sb.toString());
			if (m.find()) {
				Long duration = getTimelen(m.group(1));
				info.put("duration", duration);
			}

			String regexVideo = "Video: (.*?), (.*?), (.*?)[,\\s]";
			pattern = Pattern.compile(regexVideo);
			m = pattern.matcher(sb.toString());
			if (m.find()) {
				String group = m.group(3);
				String[] strings = group.split("x");
				info.put("width", Double.valueOf(strings[0]));
				info.put("height", Double.valueOf(strings[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	// 格式:"00:00:10.68"
	private static Long getTimelen(String timelen) {
		int min = 0;
		String[] strs = timelen.split(":");
		if (strs[0].compareTo("0") > 0) {
			// 秒
			min += Integer.parseInt(strs[0]) * 60 * 60;
		}
		if (strs[1].compareTo("0") > 0) {
			min += Integer.parseInt(strs[1]) * 60;
		}
		if (strs[2].compareTo("0") > 0) {
			min += Math.round(Float.parseFloat(strs[2]));
		}
		return (long) (min * 1000);
	}

	public static String cutAudio(Integer start, Integer end, String path, Integer id, String suffix) {
		File f = new File(path);
		boolean canUseSuffix = f.getName().contains("-cutting");
		String finalPath = f.getParentFile() + File.separator + id;
		if (canUseSuffix){
			finalPath += ".m4a";
		}else {
			finalPath += suffix;
		}
		List<String> command = new ArrayList<>();
		command.add(FFMPEG_TOOL_PATH);
		command.add("-i");
		command.add(path);
		command.add("-y");
		command.add("-acodec");
		command.add("copy");
		command.add("-ss");
		command.add(DateFormatter.secToTime(start));
		command.add("-t");
		command.add(DateFormatter.secToTime(end));
		command.add(finalPath);
		System.out.println(command);
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			Process p = builder.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = br.readLine()) != null) ;
			p.waitFor();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new File(finalPath).getPath();
	}

	public static String cutAndMerge(Videos videos, Bgms bgms) {
		String videoTmpPath = cutVideoNoAudio(0,(int) (videos.getVideoSeconds() / 1000),videos.getVideoPath(),videos.getBgmId(),"-tmp.mp4");
		String audioTmpPath = cutAudio(0, (int) (videos.getVideoSeconds() / 1000), bgms.getBgmPath(), bgms.getId(), "-tmp.m4a");
		File tmpVideo = new File(videoTmpPath);
		File tmpAudio = new File(audioTmpPath);
		String mergePath = mergeVideoAndAudio(videoTmpPath,audioTmpPath,tmpVideo.getParentFile()+File.separator+videos.getId()+".mp4");
		if (tmpVideo.exists()){
			tmpVideo.delete();
		}
		if (tmpAudio.exists()){
			tmpAudio.delete();
		}
		return mergePath;
	}

	private static String mergeVideoAndAudio(String videoPath, String audioPath, String finalPath) {
		List<String> command = new ArrayList<>();
		command.add(FFMPEG_TOOL_PATH);
		command.add("-y");
		command.add("-i");
		command.add(videoPath);
		command.add("-i");
		command.add(audioPath);
		command.add("-vcodec");
		command.add("copy");
		command.add("-acodec");
		command.add("copy");
		command.add(finalPath);

		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			Process p = builder.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line = "";
			while ((line = br.readLine()) != null) ;

			p.waitFor();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalPath;
	}


	public static String cutVideoNoAudio(Integer start, Integer end, String path, Integer id, String suffix) {
		File f = new File(path);
		String finalPath = f.getParentFile() + File.separator + id + suffix;
		List<String> command = new ArrayList<>();
		command.add(FFMPEG_TOOL_PATH);
		command.add("-i");
		command.add(path);
		command.add("-y");
		command.add("-vcodec");
		command.add("copy");
		command.add("-ss");
		command.add(DateFormatter.secToTime(start));
		command.add("-t");
		command.add(DateFormatter.secToTime(end));
		command.add("-an");
		command.add(finalPath);

		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			Process p = builder.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line = "";
			while ((line = br.readLine()) != null) ;

			p.waitFor();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new File(finalPath).getPath();
	}


//	public static void main(String[] args) {
//		getVideoInfo("M:\\file\\lifeshow\\video\\video\\29.mp4");
//	}
}
