package com.weather.service;

import java.util.List;

import com.weather.po.Weather;

/**
 * 天气操作业务类
 * @author Administrator
 *
 */
public interface WeatherService {

	/**
	 * 批量添加天气信息
	 * @param weathers
	 */
	public void baticInsert(List<Weather> weathers);
	
	/**
	 * 获取当天的天气信息
	 * @return
	 */
	public List<Weather> getOneDayWeathers();
}
