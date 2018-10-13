package com.weather.unit;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * json操作类
 * @author Administrator
 *
 */
public class JsonUnit {
	
	@Autowired
	private static Gson gson = new Gson();
	
	/**
	 * json字符串转对象
	 * @param jsonChar
	 * @param t
	 * @return
	 */
	public static <T> T jsonToObj(String jsonChar) {
		Type type = new TypeToken<T>(){}.getType();
		return gson.fromJson(jsonChar, type);
	}
	/**
	 * 对象转字符串
	 * @param object
	 * @return
	 */
	public static String objToJson(Object object) {
		return gson.toJson(object);
	}

}
