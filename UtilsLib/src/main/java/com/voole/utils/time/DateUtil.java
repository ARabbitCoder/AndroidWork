package com.voole.utils.time;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	public static String getDateTime() {
		String datetime = null;
		long time = System.currentTimeMillis();
		datetime = String.format("%d", time);
		return datetime;
	}
	
	public static long getCurrentMsec(){
		return System.currentTimeMillis();
	}
	
	public static String getCurrentMsecString(String pattern){
		long time = System.currentTimeMillis();
		String datetime = DateFormat.format(pattern, time).toString();
		return datetime;
	}
	
	public static String msec2String(long msec, String pattern) {
		String datetime = DateFormat.format(pattern, msec).toString();
		return datetime;
	}
	
	public static long string2Msec(String t, String pattern) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
		try {
			if(simpledateformat != null && !"".equals(t)){
				return simpledateformat.parse(t).getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static String secondToString(int second){
		int s = second % 60;
		int m = second / 60 % 60;
		int h = second / 60 / 60;
		return (h<10 ? "0" + h : h)  + ":" + (m<10 ? "0" + m : m ) + ":" + (s<10 ? "0" + s : s);  
	}
	
	/**
	 * 获取当前是星期几
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
	        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(dt);
	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w < 0)
	            w = 0;
	        return weekDays[w];
	}
	
	public static long string2UTCLong(String t, String pattern){
		SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			simpledateformat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
			date = simpledateformat.parse(t);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
		return date.getTime();
	}
}
