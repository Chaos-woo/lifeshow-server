package per.chao.lifeshow.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/27 10:02
 **/
public class DateObtainUtils {
	public static Long obtainMillWithZero(int days) {
		String dateTime = obtainDateString(days);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(dateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();
	}

	public static String obtainDateString(int days){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		calendar.add(Calendar.DATE, days);
		return sdf.format(calendar.getTime());
	}

	public static void main(String[] args) {
		System.out.println(obtainDateString(0));
		System.out.println(obtainMillWithZero(0));
		System.out.println(obtainMillWithZero(-1));
		System.out.println(System.currentTimeMillis());
	}
}
