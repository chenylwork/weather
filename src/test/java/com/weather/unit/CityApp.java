package com.weather.unit;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.weather.mapper.CityMapper;
import com.weather.po.City;

public class CityApp {
	
	private static final String SHENG = "01"; // 省级代码
	private static final String SHI = "02"; // 市级代码
	private static final String XIAN = "03"; // 县、区级代码
	
	private SqlSession session;

	@After
	public void after() {
	}

	@Before
	public void before() {
		Reader reader;
		try {
			reader = Resources.getResourceAsReader("mybatis-conf.xml");
			SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(reader);
			session=factory.openSession();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 添加省级城市
	 */
	@Test
	public void test1() {
		CityMapper cityMapper = session.getMapper(CityMapper.class);
		String data = UrlUnit.getData("http://www.weather.com.cn/data/list3/city.xml");
		String[] citys = data.split(",");
		for (int i = 0; i < citys.length; i++) {
			City city = new City();
			city.setCode(citys[i].substring(0, 2));
			city.setName(citys[i].substring(3));
			city.setType(SHENG);
			cityMapper.insert(city);
		}
		session.commit();
	}
	/**
	 * 添加市级城市
	 */
	@Test
	public void test2() {
		CityMapper cityMapper = session.getMapper(CityMapper.class);
		List<City> allCity = cityMapper.getAll(SHENG);
		allCity.forEach(data -> {
			String buffer = UrlUnit.getData("http://www.weather.com.cn/data/list3/city"+data.getCode()+".xml");
			String[] citys = buffer.split(",");
			for (int i = 0; i < citys.length; i++) {
				City city = new City();
				city.setCode(citys[i].substring(0, 4));
				city.setName(citys[i].substring(5));
				city.setType(SHI);
				cityMapper.insert(city);
				System.out.println(city);
			}
			session.commit();
		});
	}
	
	/**
	 * 添加县级城市
	 * 3101
	 */
	@Test
	public void test3() {
		CityMapper cityMapper = session.getMapper(CityMapper.class);
		List<City> allCity = cityMapper.getAll(SHI);
		allCity.forEach(data -> {
			String buffer = UrlUnit.getData("http://www.weather.com.cn/data/list3/city"+data.getCode()+".xml");
			String[] citys = buffer.split(",");
			for (int i = 0; i < citys.length; i++) {
				City city = new City();
				city.setCode(citys[i].substring(0, 6));
				city.setName(citys[i].substring(7));
				city.setType(XIAN);
				cityMapper.insert(city);
				System.out.println(city);
			}
			session.commit();
		});
	}
	
	/**
	 * www.weather.com.cn/data/list3/city010101.xml
	 * 获取天气代码
	 */
	@Test
	public void test4() {
		CityMapper cityMapper = session.getMapper(CityMapper.class);
		List<City> allCity = cityMapper.getAll(XIAN);
		allCity.forEach(city -> {
			String buffer = UrlUnit.getData("http://www.weather.com.cn/data/list3/city"+city.getCode()+".xml");
			String[] citys = buffer.split(",");
			for (int i = 0; i < citys.length; i++) {
				city.setWeatherCode(citys[i].substring(7));
				cityMapper.update(city);
				session.commit();
				System.out.println(city);
			}
		});
	}
}
