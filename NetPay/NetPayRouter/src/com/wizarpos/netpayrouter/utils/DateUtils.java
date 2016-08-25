package com.wizarpos.netpayrouter.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author xuchuanren
 * @date 2015年11月13日 上午11:23:11
 * @modify 修改人： 修改时间：
 */
public class DateUtils {

	public static String time_format = "yyyyMMdd";
	public static String time_format1 = "yyyy-MM-dd HH:mm:ss";
	
	public DateUtils() {
		// TODO Auto-generated constructor stub
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

	}

	/**
	 * @return yyyyMMdd
	 */
	public static String getCreateDate(String format) {
		Date currentTime = new Date();
		SimpleDateFormat formatter1 = new SimpleDateFormat(format);
		return formatter1.format(currentTime);
	}

	
	/**
	 * 日期格式字符串转换成时间戳
	 * 
	 * @param date
	 *            字符串日期
	 * @param format
	 *            如：yyyy-MM-dd HH:mm:ss
	 * @return 格式
	 */
	public static String date2TimeStamp(String date_str) {
		try {
			String format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			return String.valueOf(sdf.parse(date_str).getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static String TimeStamp2date(String date_str) {
		try {
			String format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			return sdf.format(new Date(Long.valueOf(date_str)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	// 获得当天0点时间
		public static Date getTimesmorning(boolean isYesterday) {
			Calendar cal = Calendar.getInstance();
			if(isYesterday){
				cal.add(Calendar.DATE, -1);
			}
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		}

		// 获得当天24点时间
		public static Date getTimesnight(boolean isYesterday) {
			Calendar cal = Calendar.getInstance();
			if(isYesterday){
				cal.add(Calendar.DATE, -1);
			}
			cal.set(Calendar.HOUR_OF_DAY, 24);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return  cal.getTime();
		}

		// 获得本周一0点时间
		public static Date getTimesWeekmorning(boolean isLast) {
			Calendar cal = Calendar.getInstance();
			if(isLast){
			  cal.add(Calendar.WEEK_OF_YEAR, -1);// 一周    
			}
			
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			return  cal.getTime();
		}

		// 获得本周日24点时间
		public  static Date getTimesWeeknight(boolean isLast) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getTimesWeekmorning(isLast));
			cal.add(Calendar.DAY_OF_WEEK, 7);
			return cal.getTime();
		}

		// 获得本月第一天0点时间
		public static Date getTimesMonthmorning(boolean lastmonth) {
			Calendar cal = Calendar.getInstance();
			if(lastmonth){
				cal.add(Calendar.MONTH, -1);
			}
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			return  cal.getTime();
		}

		// 获得本月最后一天24点时间
		public static Date getTimesMonthnight(boolean lastmonth) {
			Calendar cal = Calendar.getInstance();
			if(lastmonth){
				cal.add(Calendar.MONTH, -1);
			}
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.HOUR_OF_DAY, 24);
			return cal.getTime();
		}
	
	
}
