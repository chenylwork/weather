package com.weather.mapper;

import java.util.List;

import com.weather.po.City;

/**
 * 城市操作mapper
 * @author Administrator
 *
 */
public interface CityMapper {
	/**
	 * 添加城市信息
	 * @param city
	 * @return
	 */
	public boolean insert(City city);
	/**
	 * 根据参数获取城市信息
	 * 
	 * @param type 城市类型
	 * @return
	 */
	public List<City> getAll(String type);
	/**
	 * 修改城市信息
	 * @param city
	 * @return
	 */
	public boolean update(City city);
}
