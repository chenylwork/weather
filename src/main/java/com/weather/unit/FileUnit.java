package com.weather.unit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * 
 * @author Administrator
 *
 */
public class FileUnit {
	/**
	 * 获取文件夹下的所有文件，不进行递归处理子文件夹
	 * 
	 * @param filePath
	 * @return List<String> 子文件的文件名称集合
	 */
	public static List<String> printFile(String filePath) {
		List<String> list = new ArrayList<>();
		File file = new File(filePath);
		if (file.isDirectory()) {
			String[] fileNameList = file.list();
			for (int i = 0; i < fileNameList.length; i++) {
				String childFileName = fileNameList[i];
				File childFile = new File(filePath + "/" + childFileName);
				if (childFile.isFile()) {
					list.add(childFileName);
				}
			}
		}
		return list;
	}

	/**
	 * 创建目录
	 * @param path
	 */
	public static void mkdirs(String path) {
		File file = new File(path);
		if(!file.exists()) file.mkdirs();
	}
	/**
	 * 获取父级目录
	 * @param path 需要获取的文件
	 * @param index 向上查找层数
	 * @return
	 */
	public static String getParentPath(File file,int index) {
		for(int i = 0; i<index; i++) {
			file = file.getParentFile();
		}
		return file.getPath();
	}
	/**
	 * 获取父级目录
	 * @param path 需要获取的文件路径
	 * @param index 向上查找层数
	 * @return
	 */
	public static String getParentPath(String path,int index) {
		return getParentPath(new File(path),index);
	}

}
