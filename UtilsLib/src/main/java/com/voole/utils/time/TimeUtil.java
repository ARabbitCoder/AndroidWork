package com.voole.utils.time;

import com.voole.utils.log.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 将时间转换工具类
 * @author lichao
 *
 */
public class TimeUtil {

	/**
	 * 
	  * @param currentPosition 当前时间
	  * @return 返回按 照时间显示的字符串
	  * @description 通过时间显示用户当前拖动到影片那里.
	  * @version 1.0
	  * @author
	  * @data 2016-8-22上午10:43:34
	  * @update 上午10:43:34
	 */
	public static String currentPostionToPlayTime(int currentPosition) {
		currentPosition /= 1000;
		int minute = currentPosition / 60;
		int hour = minute / 60;
		int second = currentPosition % 60;
		minute %= 60;
		return String.format("%02d:%02d:%02d", hour, minute,second);
	}

	/**
	 *
	 * @param currentPosition 当前时间
	 * @return 返回按 照时间显示的字符串 不到小时单位
	 * @description 通过时间显示用户当前拖动到影片那里.
	 * @version 1.0
	 * @author
	 * @data 2016-8-22上午10:43:34
	 * @update 上午10:43:34
	 */
	public static String currentPostionToPlayTimeShort(int currentPosition) {
		currentPosition /= 1000;
		int minute = currentPosition / 60;
		int hour = minute / 60;
		int second = currentPosition % 60;
		minute %= 60;
		return String.format("%02d:%02d",minute,second);
	}

	/**
	 * 
	  * @param allDistance 总时长
	  * @param seekToPosition 用户拖动到的位置(或者是videoview需要seekTo的位置)
	  * @return 返回 按照百分比显示的值
	  * @description 通过百分比显示用户当前拖动到影片那里.
	  * @version 1.0
	  * @author
	  * @data 2016-8-22上午10:43:34
	  * @update 上午10:43:34
	 */
	public static int showVideoProgressByPercent(float allDistance,int seekToPosition) {
		float percentageFloat = ((float) seekToPosition / allDistance);
		 if (percentageFloat > 1) {
			 percentageFloat = 1;
		 } else if (percentageFloat < 0) {
			 percentageFloat = 0;
		 }
		 int percentageInt = (int) (percentageFloat * 100);
		return percentageInt;
	}
	
	/**
	 * 
	 * @return
	 * @description 将系统当前时间造型成字符串（TODO:用来得到直播模式下右部显示的字符串）
	 * @version 1.0
	 * @author houjie
	 * @date 2016-10-21 下午1:58:15 
	 * @update 2016-10-21 下午1:58:15
	 */
	
	public static String currentSystemToPlayTime(){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar calendar = Calendar.getInstance();
		  int hour = calendar.get(Calendar.HOUR_OF_DAY);
		  SimpleDateFormat df;
		  if(hour>12){
			  df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		  }else{
			  df = new SimpleDateFormat("hh:mm:ss");//设置日期格式
		  }
		return df.format(calendar.getTime());
	}
	/**
	 * 
	 * @param n
	 * @return
	 * @description 将当前时间的n小时前时间造型成字符串（TODO:用来得到直播模式下左部显示的字符串）
	 * @version 1.0
	 * @author houjie
	 * @date 2016-10-21 下午1:57:48 
	 * @update 2016-10-21 下午1:57:48
	 */
	public static String beforeNHoursToPlayTime(int n){
		  Calendar calendar = Calendar.getInstance();
		  int hour = calendar.get(Calendar.HOUR_OF_DAY);
		  SimpleDateFormat df;
		  if(hour>12){
			  df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		  }else{
			  df = new SimpleDateFormat("hh:mm:ss");//设置日期格式
		  } 
		  calendar.add(Calendar.HOUR, n);
		return df.format(calendar.getTime());
	}
	/**
	 * 
	 * @param n
	 * @param currentProgress
	 * @return
	 * @description 将当前时间的n小时前+time时间后的时间造型成字符串（TODO:用来得到直播模式下滑动中间改变进度时应该显示的字符串）
	 * @version 1.0
	 * @author houjie
	 * @date 2016-10-21 下午1:55:53 
	 * @update 2016-10-21 下午1:55:53
	 */
	public static String beforeNHoursAndAfterTime(int n,int currentProgress){
		long time1 = beforeNHoursToPlayTime(n, currentProgress);
		Date date = new Date(time1);
		SimpleDateFormat df;
		if(date.getHours()>12){
			  df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		  }else{
			  df = new SimpleDateFormat("hh:mm:ss");//设置日期格式
		 } 
		return df.format(date);
	}
	/**
	 * 
	  * @param currentProgress
	  * @return 返回一个长整形的数字.用户拖动以后的时间.
	  * @description
	  * @version 1.0
	  * @author lichao
	  * @data 2016-11-15上午11:32:42
	  * @update 上午11:32:42
	 */
	public static long beforeNHoursToPlayTime(int nHours ,int currentProgress){
		//movePoint = Math.abs(sysTime-(timeOffet -progress));
		long currentTime = System.currentTimeMillis();
		currentTime = Math.abs(currentTime-(nHours*60*60*1000 -currentProgress));
		return currentTime;
	}
}
