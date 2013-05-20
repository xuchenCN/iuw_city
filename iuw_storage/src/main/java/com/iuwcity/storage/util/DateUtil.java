/*
 * @(#)DateUtil.java 1.00 2004-04-12 Copyright (c) 2005 Shenzhen Surfilter
 * Network Technology Co.,Ltd. All rights reserved.
 */
package com.iuwcity.storage.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	public static final DateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

	public static final DateFormat DEFAULT_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static final DateFormat DATE_FORMAT_MONTH = new SimpleDateFormat("yyyyMM");

	private DateUtil() {
	}

	public static final String getYMD() {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(now);
	}

	/*
	 * Date --> String
	 */
	public synchronized static String date2String(Date date, DateFormat dateFormat) {
		if (date == null)
			return null;
		return dateFormat.format(date);
	}

	public static String date2String(Date date, String dateFormat) {
		if (date == null)
			return null;
		return date2String(date, new SimpleDateFormat(dateFormat));
	}

	public static String date2String(Date date) {
		if (date == null)
			return null;
		return date2String(date, DEFAULT_DATE_FORMAT);
	}

	public static String date2StringOther(Date date) {
		if (date == null)
			return null;
		return date2String(date, DATETIME_FORMAT);
	}

	public static String date2StringAsyyyyMMdd(Date date) {
		if (date == null)
			return null;
		return date2String(date, DATE_FORMAT);
	}

	public static String date2StringAsyyyyMM(Date date) {
		if (date == null)
			return null;
		return date2String(date, DATE_FORMAT_MONTH);
	}
	
	public synchronized static String time2String(Date time, DateFormat dateFormat) {
		if (time == null)
			return null;
		return dateFormat.format(time);
	}

	public static String time2String(Date time, String dateFormat) {
		if (time == null)
			return null;
		return date2String(time, new SimpleDateFormat(dateFormat));
	}

	public static String time2String(Date time) {
		if (time == null)
			return null;
		return date2String(time, DEFAULT_TIME_FORMAT);
	}

	public synchronized static String dateTime2String(Date dateTime, DateFormat dateFormat) {
		if (dateTime == null)
			return null;
		return dateFormat.format(dateTime);
	}

	public static String dateTime2String(Date dateTime, String dateFormat) {
		if (dateTime == null)
			return null;
		return date2String(dateTime, dateFormat);
	}

	public static String dateTime2String(Date dateTime) {
		if (dateTime == null)
			return null;
		return date2String(dateTime, DEFAULT_DATETIME_FORMAT);
	}

	/*
	 * String -->Date
	 */

	public synchronized static Date string2Date(String date, DateFormat dateFormat) {
		if (date == null)
			return null;
		try {
			return dateFormat.parse(date);
		}
		catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Date string2Date(String date, String dateFormat) {
		return string2Date(date, new SimpleDateFormat(dateFormat));
	}

	public static Date string2Date(String date) {
		return string2Date(date, DEFAULT_DATE_FORMAT);
	}

	public static Time string2Time(String time, DateFormat timeFormat) {
		if (time == null)
			return null;
		return new Time(string2Date(time, timeFormat).getTime());
	}

	public static Time string2Time(String time, String timeFormat) {
		if (time == null)
			return null;
		return new Time(string2Date(time, timeFormat).getTime());
	}

	public static Time string2Time(String time) {
		return string2Time(time, DEFAULT_TIME_FORMAT);
	}

	public static Timestamp string2DateTime(String time, DateFormat timeFormat) {
		if (time == null)
			return null;
		return new Timestamp(string2Date(time, timeFormat).getTime());
	}

	public static Timestamp string2DateTime(String time, String timeFormat) {
		if (time == null)
			return null;
		return new Timestamp(string2Date(time, timeFormat).getTime());
	}

	public static Timestamp string2DateTime(String time) {
		return string2DateTime(time, DEFAULT_DATETIME_FORMAT);
	}

	public static Timestamp string2DateTimeOther(String time) {
		return string2DateTime(time, DATETIME_FORMAT);
	}

	public synchronized static String getCurrentDateAsString() {
		return DEFAULT_DATE_FORMAT.format(Calendar.getInstance().getTime());
	}

	public synchronized static String getCurrentDateTimeAsString() {
		return DEFAULT_DATETIME_FORMAT.format(Calendar.getInstance().getTime());
	}

	public static String getCurrentDateAsString(String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(Calendar.getInstance().getTime());
	}

	public static String getDateString(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	public static Date parseDate(String date, DateFormat df) {
		try {
			return df.parse(date);
		}
		catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Date parseDate(String date, String dateFormat) {
		SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
		return parseDate(date, fmt);
	}

	public synchronized static Date parseDate(String date) {
		return parseDate(date, DEFAULT_DATETIME_FORMAT);
	}

	public static Timestamp nowTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	public static String toString(Date time) {
		return getDateString(time, "yyyy-MM-dd HH:mm:ss");
	}

	public static String fromUnixTime(Long ms) {
		return getDateString(new Date(ms.longValue() * 1000), "yyyy-MM-dd HH:mm:ss");
	}

	public static Long unixTimestamp(String date) {
		return new Long(parseDate(date).getTime() / 1000);
	}

	public static Long unixTimestamp(String date, String dateFormat) {
		return new Long(parseDate(date, dateFormat).getTime() / 1000);
	}

	public static Long currentUnixTimestamp() {
		return new Long(System.currentTimeMillis() / 1000);
	}

	public static Long unixTimestamp(Date date) {
		return new Long(date.getTime() / 1000);
	}

	public static String getTimeStampNumberFormat(Timestamp formatTime) {
		SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss", new Locale("zh", "cn"));
		return m_format.format(formatTime);
	}
	
	public static String getTimeStampAsString(Timestamp formatTime) {
		SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return m_format.format(formatTime);
	}
	
	public static String getDateAsString(Date formatTime) {
		SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return m_format.format(formatTime);
	}
	
	/**
	 * 获得上一个时段的字符串，例：2011032117
	 * @return
	 */
	public static String getLastHour(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");
		Calendar cl = Calendar.getInstance();
		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH),cl.get(cl.DAY_OF_MONTH),cl.get(cl.HOUR_OF_DAY)-1,0,0);
		return sf.format(cl.getTime());
	}

	/**
	 * 获得相差天数
	 */
	public static int getTimeDifference(Timestamp endTime, Timestamp startTime) {
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
		long t1 = 0L;
		long t2 = 0L;
		int days = 0;
		try {
			t1 = timeformat.parse(getTimeStampNumberFormat(endTime)).getTime();
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t2 = timeformat.parse(getTimeStampNumberFormat(startTime)).getTime();
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 因为t1-t2得到的是毫秒�?�?��要初3600000得出小时.算天数或秒同�?
		if ((int) ((t1 - t2) % (3600000 * 24)) == 0) {
			days = (int) ((t1 - t2) / (3600000 * 24));
		} else {
			days = (int) ((t1 - t2) / (3600000 * 24)) + 1;
		}
		// int hours = (int) ((t1 - t2) / 3600000);
		// int minutes = (int) (((t1 - t2) / 1000 - hours * 3600) / 60);
		return days;
	}
	
	public static void main(String args[]){
		System.out.println(DateUtil.getLastHour());
	}
}
