package com.weather.unit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间操作工具类
 * @author Administrator
 *
 */
public class DateUnit {
	
	/**
	 * 获取日历对象的给定字段的数值
	 * @param date 时间对象
	 * @param field 需要获取的字段
	 * @return int
	 */
	public static int get(Date date,int field) {
		Calendar calendar = dateParseCalendar(date);
		return calendar.get(field);
	}
	
	/**
	 * 将给定格式的日历的给定字段设置为给定的值
	 * @param source 需要修改的时间字符串
	 * @param pattern 时间格式
	 * @param field 需要修改的给定字段，详情请参见{@link Calendar#set(int, int)}方法的第一个参数
	 * @param value 设置值，详情请参见{@link Calendar#set(int, int)}方法的第二个参数
	 * @return
	 */
	public static String setDate(String source,String pattern,int field,int value) {
		Date date = parseDate(source, pattern);
		date = setDate(date, field, value);
		return parseString(date, pattern);
	}
	
	/**
	 * 将给定日历的给定字段设置为给定的值
	 * @param date 给定日历
	 * @param pattern 时间格式
	 * @param field 需要修改的给定字段，详情请参见{@link Calendar#set(int, int)}方法的第一个参数
	 * @param value 设置值，详情请参见{@link Calendar#set(int, int)}方法的第二个参数
	 * @return
	 */
	public static Date setDate(Date date,int field,int value) {
		Calendar calendar = dateParseCalendar(date);
		return setDate(calendar,field,value);
	}
	
	/**
	 * 将给定日历的给定字段设置为给定的值
	 * @param date 日历
	 * @param field 需要修改的给定字段，详情请参见{@link Calendar#set(int, int)}方法的第一个参数
	 * @param value 设置值，详情请参见{@link Calendar#set(int, int)}方法的第二个参数
	 * @return
	 * @throws ParseException
	 */
	private static Date setDate(Calendar calendar,int field,int value) {
		calendar.set(field, value);
		return calendar.getTime();
	}
	/**
	 * 将给定格式的日历的给定字段添加给定的值
	 * @param source 需要修改的时间字符串
	 * @param pattern 时间格式
	 * @param field 需要修改的给定字段，详情请参见{@link Calendar#set(int, int)}方法的第一个参数
	 * @param value 设置值，详情请参见{@link Calendar#set(int, int)}方法的第二个参数
	 * @return
	 */
	public static String addDate(String source,String pattern,int field,int value) {
		Date date = parseDate(source, pattern);
		date = addDate(date, field, value);
		return parseString(date, pattern);
	}
	/**
	 * 将给定的日历的给定字段添加给定的值
	 * @param date 需要修改的时间字符串
	 * @param field 需要修改的给定字段，详情请参见{@link Calendar#set(int, int)}方法的第一个参数
	 * @param value 设置值，详情请参见{@link Calendar#set(int, int)}方法的第二个参数
	 * @return
	 */
	public static Date addDate(Date date,int field,int value) {
		Calendar calendar = dateParseCalendar(date);
		return addDate(calendar,field,value);
	}
	/**
	 * 修改指定字段的值
	 * @param calendar 需要修改的时间字符串
	 * @param field 需要修改的给定字段，详情请参见{@link Calendar#set(int, int)}方法的第一个参数
	 * @param value 设置值，详情请参见{@link Calendar#set(int, int)}方法的第二个参数
	 * @return
	 */
	private static Date addDate(Calendar calendar,int field,int value) {
		calendar.add(field, value);
		return calendar.getTime();
	}
	
	
	
	/**
	 * 将给定格式的时间字符串转换为时间Date对象
	 * @param source 时间字符串
	 * @param pattern 字符串对应时间的格式 
	 * 详情请参见{@link SimpleDateFormat#SimpleDateFormat(String)}的构造参数
	 * @return 
	 * @throws ParseException
	 */
	public static Date parseDate(String source,String pattern){
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			date = dateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 根据时间对象获取对应的操作对象
	 * @param date
	 * @return
	 */
	private static Calendar dateParseCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
	/**
	 * 根据转换格式获取时间字符串
	 * @param date 需要获取的时间
	 * @param pattern 转换格式 比如yyyy-MM-dd 
	 * @return
	 */
	public static String parseString(Date date,String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	

}
