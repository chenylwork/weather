package com.weather.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weather.mapper.WeatherMapper;
import com.weather.po.Weather;
import com.weather.service.WeatherService;
import com.weather.unit.DateUnit;

/**
 * 天气操作实现类
 * @author Administrator
 *
 */
@Service("WeatherService")
public class WeatherServiceImpl implements WeatherService{

	@Autowired
	private WeatherMapper weatherMapper;
	/**
	 * 添加天气信息
	 */
	@Override
	@Transactional
	public void baticInsert(List<Weather> weathers) {
		for(Weather weather : weathers) {
			if(weather == null) {
				return;
			}
			// 查看是否存在该时间天气
			Weather oldWeather = weatherMapper.getByDayAndHour(weather.getDay(), weather.getOd21());
			// 不存在添加，存在修改
			if(oldWeather == null) {
				weatherMapper.insert(weather);
			} else {
				weatherMapper.update(weather);
			}
		}
	}
	
	@Override
	@Transactional
	public List<Weather> getOneDayWeathers() {
		Date date = new Date();
		// 小时确定为10
		//date = DateUnit.setDate(date, Calendar.HOUR_OF_DAY, 10);
		String pattern = "yyyyMMddHH";
		String start = DateUnit.parseString(date, pattern);
		// 天数加一
		date = DateUnit.addDate(date, Calendar.DAY_OF_MONTH, 1);
		String end = DateUnit.parseString(date, pattern);
		
		return weatherMapper.getOneDayWeather(start,end);
	}

}
