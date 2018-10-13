package com.weather.task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weather.po.Weather;
import com.weather.service.WeatherService;

/**
 * 天气数据操作
 * @author Administrator
 *
 */
public class WeatherTask {
	
	@Autowired
	private Gson gson;
	
	@Autowired
	private WeatherService weatherService;
	/**
	 * 定时执行的任务
	 */
	public void job() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				getData();
			}
		};
		new Thread(runnable).start();
	}
	
	/**
	 * 从气象网上获取web数据
	 */
	public void getData() {
		Document document = getDynamicDoc();
		try {
			document = Jsoup.connect("http://www.weather.com.cn/weather/101090101.shtml").get();
			// 解析dom获取所需信息
			Elements allElements = document.getElementsByTag("script");
			// 分时段预报数据
//			getHour3data(allElements);
			getObserve24hData(allElements);
//			allElements.forEach(node -> {
//				System.out.println(node);
//			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 24小时的整点天气实况数据
	 */
	public void getObserve24hData(Elements allElements) {
		Map<String, Object> map = resolve(4,allElements);
		// 解析出本地区数据po
		String mapStr = gson.toJson(map.get("od"));
		Type mapType = new TypeToken<Map<String,Object>>(){}.getType();
		Map<String,Object> childNode = gson.fromJson(mapStr, mapType);
		
		// 解析出数据对应时间（天）
		String dateChar = ((String)childNode.get("od0"));
		
		// 解析出每小时的天气信息集合
		String weathersJsonChar = gson.toJson(childNode.get("od2"));
		Type listType = new TypeToken<List<Weather>>(){}.getType();
		List<Weather> weatherList = gson.fromJson(weathersJsonChar, listType);
		
		// 修改数据对应时间（天）
		updateWeatherDay(weatherList,dateChar);
		// 天气集合业务操作
		weatherList.forEach(data -> System.err.println(data));
		
		weatherService.baticInsert(weatherList);
		System.out.println("执行结束");
		
	}
	/**
	 * 时间对应本月中的时间加一天
	 * @param day
	 * @return
	 */
	public String addOneDay(String day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = dateFormat.parse(day);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);
			Date time = calendar.getTime();
			return dateFormat.format(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return day;
	}
	/**
	 * 修改数据的时间点
	 * @param weatherList 天气数据集合
	 * @param dataDay 参照时间
	 */
	public List<Weather> updateWeatherDay(List<Weather> weatherList,String dataDay) {
		// 解析出数据对应时间
		String weatherDate = dataDay.substring(0,10);
		String weatherDay = weatherDate.substring(0,8);
		// 获取对应的小时时间点
		int nowHour = Integer.parseInt(dataDay.substring(weatherDate.length()-2,weatherDate.length()));
		for(Weather weather : weatherList) {
			if(weather != null) {
				int isChange = checkHour(weather,nowHour);
				// 设置天气时间
				weather.setDay((isChange == 1) ? addOneDay(weatherDay) : weatherDay);
			}
		}
		return weatherList;
	}
	/**
	 * 检验天气对象时间的修改操作
	 * 当返回0时表示对象为当前时间的同一天数据信息
	 * 当返回1时表示对象为当前时间的后一天数据信息
	 * @param weather 天气对象
	 * @param nowHour 当前时间点
	 * @return int 0 或 1
	 */
	public int checkHour(Weather weather,int nowHour) {
		String od21 = weather.getOd21();
		int dataHour = Integer.parseInt(od21);
		// 数据的小时点大于参照时间的，为参照时间同一天的天气信息
		if(dataHour > nowHour) {
			return 0;
		} 
		// 数据的小时点小于参照时间的，为参照时间同后一天的天气信息
		if(dataHour < nowHour) {
			return 1;
		}
		// 数据的小时点等于参照时间的，且od28字段为空的为后一天的同一时间点的天气信息
		// 否则为当前时间点的天气信息
		if(dataHour == nowHour && "".equals(weather.getOd28())) {
			return 1;
		} else {
			return 0;
		}
	}
	/**
	 * 时间段气象信息预报
	 * 时间间隔为3小时
	 */
	public void getHour3data(Elements allElements) {
//		resolve(2,allElements);
	}
	
	
	public Map<String,Object> resolve(int index,Elements allElements) {
		Element element = allElements.get(index);
		Node node = element.childNodes().get(0);
		String string = node.toString();
		String substring = string.substring(string.indexOf("=")+1).trim();
		if(substring.indexOf(";") > 0) {
			substring = substring.substring(0, substring.indexOf(";"));
		}
		Type type = new TypeToken<Map<String,Object>>(){}.getType();
		return gson.fromJson(substring, type);
	}
	/**
	 * 使用Htmlunit模拟浏览器获取动态页面
	 * @return
	 */
	@SuppressWarnings("resource")
	public Document getDynamicDoc() {
		String url = "http://www.weather.com.cn/weather/101090101.shtml";
		// Htmlunit 模拟浏览器
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 启用JavaScript解析器（默认为true）
		webClient.getOptions().setJavaScriptEnabled(true); 
		// 禁用css支持
		webClient.getOptions().setCssEnabled(false); 
		// JavaScript运行异常，不抛出异常
		webClient.getOptions().setThrowExceptionOnScriptError(false); 
		// 事件发生时是否引发异常
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 连接超时时间
		webClient.getOptions().setTimeout(6000); 
		try {
			HtmlPage page = webClient.getPage(url);
			webClient.waitForBackgroundJavaScript(30 * 1000); // 等待JavaScript后天执行30秒
			String pageasXml = page.asXml();
			return Jsoup.parse(pageasXml,url);
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
