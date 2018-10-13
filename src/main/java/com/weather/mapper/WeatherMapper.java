package com.weather.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weather.po.Weather;
/**
 * 天气操作mappper
 * @author Administrator
 *
 */
public interface WeatherMapper {
	/**
	 * 添加天气信息
	 * @param weather
	 * @return
	 */
	public boolean insert(Weather weather);
	/**
	 * 修改天气对象
	 * @param weather
	 * @return
	 */
	public boolean update(Weather weather);
	/**
	 * 根据天气对象的对应的时间（天）和对应的时间点（小时）
	 * 获取对应的对象
	 * @param day 对应的时间（天）
	 * @param hour 对应的时间（小时）
	 * @return 
	 */
	public Weather getByDayAndHour(@Param("day")String day,@Param("hour")String hour);
	
	/**
	 * 获取一天的天气信息
	 * @param start 起始时间 比如 2018101215 （2018年10月12号15时 ）
	 * @param end 结束时间 比如 2018101215 （2018年10月12号15时 ）
	 * @return
	 */
	public List<Weather> getOneDayWeather(@Param("start")String start,@Param("end")String end);
	
}
