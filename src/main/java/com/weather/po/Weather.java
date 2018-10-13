package com.weather.po;

/**
 * 用来接收整点天气状况信息对象
 * @author Administrator
 *
 */
public class Weather {
	
	private Integer id; // 
	private String day; // 天气对象对应的时间（天）
	
	private String od21; // 天气对象对应的时间（小时）
	private String od22; // 温度
	private String od23; //
	private String od24; // 风向
	private String od25; // 风力
	private String od26; // 降水量
	private String od27; // 相对湿度
	private String od28; // 空气质量
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getOd21() {
		return od21;
	}
	public void setOd21(String od21) {
		this.od21 = od21;
	}
	public String getOd22() {
		return od22;
	}
	public void setOd22(String od22) {
		this.od22 = od22;
	}
	public String getOd23() {
		return od23;
	}
	public void setOd23(String od23) {
		this.od23 = od23;
	}
	public String getOd24() {
		return od24;
	}
	public void setOd24(String od24) {
		this.od24 = od24;
	}
	public String getOd25() {
		return od25;
	}
	public void setOd25(String od25) {
		this.od25 = od25;
	}
	public String getOd26() {
		return od26;
	}
	public void setOd26(String od26) {
		this.od26 = od26;
	}
	public String getOd27() {
		return od27;
	}
	public void setOd27(String od27) {
		this.od27 = od27;
	}
	public String getOd28() {
		return od28;
	}
	public void setOd28(String od28) {
		this.od28 = od28;
	}
	@Override
	public String toString() {
		return "Weather [id=" + id + ", day=" + day + ", od21=" + od21 + ", od22=" + od22 + ", od23=" + od23 + ", od24="
				+ od24 + ", od25=" + od25 + ", od26=" + od26 + ", od27=" + od27 + ", od28=" + od28 + "]";
	}

}
