package com.weather.common;

import com.weather.unit.FileUnit;

/**
 * 配置参数获取类
 * @author Administrator
 *
 */
public class Properties {
	
	/**
	 * 雷达文件存储根路径
	 */
	public static final String RADARIMAGEROOTPATH = radarImageRootPath();
	
	
	/**
	 * 雷达文件存储根路径
	 * @return
	 */
	public static String radarImageRootPath() {
		// 项目classes路径
		String path = Properties.class.getResource("/").getPath();
		System.out.println(path);
		// 文件存储路径
		String webappsPath = FileUnit.getParentPath(path, 3);
		return webappsPath+"/images/radar/";
	}
	public static void main(String[] args) {
		String rootPath = radarImageRootPath();
		System.out.println(rootPath);
	}

}
