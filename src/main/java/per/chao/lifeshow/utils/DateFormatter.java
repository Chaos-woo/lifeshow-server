package per.chao.lifeshow.utils;


import java.text.SimpleDateFormat;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/3/27 15:48
 **/
public class DateFormatter {
	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	public static String to(Long mills) {
		return dateFormatter.format(mills);
	}

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}
	private static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

//	public static void main(String[] args) {
//		System.out.println(secToTime(67));;
//	}
}
