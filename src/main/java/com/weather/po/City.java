package com.weather.po;

/**
 * 城市对象实体类
 * @author Administrator
 *
 */
public class City {
	private Integer id; // id
	private String code; // 城市编码
	private String name; // 城市名称
	private String weatherCode; // 地区天气编码
	private String type; // 城市类型
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWeatherCode() {
		return weatherCode;
	}
	public void setWeatherCode(String weatherCode) {
		this.weatherCode = weatherCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "City [id=" + id + ", code=" + code + ", name=" + name + ", weatherCode=" + weatherCode + ", type="
				+ type + "]";
	}
	
}
