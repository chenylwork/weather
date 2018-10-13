package com.weather.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.weather.po.Weather;
import com.weather.service.WeatherService;

/**
 * 天气处理controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {

	@Autowired
	private WeatherService weatherService;
	@Autowired
	private Gson gson;
	
	@RequestMapping(value="/day",produces= MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
	@ResponseBody
	public String weather() {
		List<Weather> oneDayWeathers = weatherService.getOneDayWeathers();
		return gson.toJson(oneDayWeathers);
	}
}
