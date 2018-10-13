package com.weather.unit;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class App {
	private static String SERVICES_HOST = "www.webxml.com.cn";
	private static String WEATHER_SERVICES_URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/";
	private static String SUPPORT_CITY_URL = WEATHER_SERVICES_URL + "getSupportCity?byProvinceName=ALL";
	private static String WEATHER_QUERY_URL = WEATHER_SERVICES_URL + "getWeatherbyCityName?theCityName=";

	private App() {
	}

	public static InputStream getSoapInputStream(String url) throws Exception {
		InputStream is = null;
		URL U = new URL(url);
		URLConnection conn = U.openConnection();
		conn.setRequestProperty("Host", SERVICES_HOST);
		conn.connect();
		is = conn.getInputStream();
		return is;
	}

	// 取得支持的城市列表
	public static ArrayList<String> getSupportCity() throws Exception {
		ArrayList<String> cityList = null;
		Document doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = getSoapInputStream(SUPPORT_CITY_URL);
		doc = db.parse(is);
		NodeList nl = doc.getElementsByTagName("string");
		int len = nl.getLength();
		cityList = new ArrayList<String>(len);
		for (int i = 0; i < len; i++) {
			Node n = nl.item(i);
			String city = n.getFirstChild().getNodeValue();
			cityList.add(city);
		}
		is.close();
		return cityList;
	}

	// 取得城市的天气
	public static ArrayList<String> getWeather(String city) throws Exception {
		ArrayList<String> weatherList = null;
		Document doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		// 这里他的编码格式就是这样，我试了几个也没办法。。只好这样混过去了
		InputStream is = getSoapInputStream(WEATHER_QUERY_URL + new String(city.getBytes("UTF-8"), "GBK"));
		doc = db.parse(is);
		NodeList nl = doc.getElementsByTagName("string");
		int len = nl.getLength();
		weatherList = new ArrayList<String>(len);
		for (int i = 0; i < len; i++) {
			Node n = nl.item(i);
			String weather = n.getFirstChild().getNodeValue();
			weatherList.add(weather);
		}
		is.close();
		return weatherList;
	}

	public static void main(String[] args) throws Exception {
		ArrayList<String> weatherList = App.getWeather("53698");
//		ArrayList<String> weatherList = App.getSupportCity();
		for (String weather : weatherList) {
			System.out.println(weather);
		}
	}
}
