package com.weather.unit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.weather.common.Properties;

public class RadarImgApp {
	/**
	 * 定时执行的任务方法
	 */
	public static void job(Date date) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				executeJob(date);
			}
		};
		new Thread(runnable).start();
	}

	public static void main(String[] args) {
		Date date = new Date();
		while (true) {
			date = DateUnit.addDate(date, Calendar.MINUTE, -6);
			executeJob(date);
			int day = DateUnit.get(date, Calendar.DAY_OF_MONTH);
			if (day < 10) {
				return;
			}
		}
	}

	/**
	 * 生成雷达图像获取时间戳编码，气象网的雷达图每6分钟产生一张雷达图 获取当前时间的雷达图格式 20181013000600001 2018 10 13 00
	 * 06 00 001 年 月 日 时 分 秒 当前编码获取的雷达图对应的时间为 2018年10月13日08时06分00秒的雷达图
	 * 
	 * 获取的时间编码。 为当前时间的小时数减去8 当前时间的分钟数减去分钟数除以6的余数
	 * 
	 * @return
	 */
	public static String getRequestParam(Date date) {
		// 改变日历对象
		date = changeDate(date);
		// 日历对象的小时减8
		date = DateUnit.addDate(date, Calendar.HOUR_OF_DAY, -8);
		String dateNum = DateUnit.parseString(date, "yyyyMMddHHmm");
		return dateNum + "00001";
	}

	/**
	 * 网络获取的文件本地存储文件对象
	 * 
	 * @param nowDateNum
	 *            时间戳对应时间码 201809101212 年月日时分
	 * @return
	 */
	public static File getImgFile(Date date) {
		// 改变日历对象
		date = changeDate(date);
		String fileName = DateUnit.parseString(date, "yyyyMMddHHmm");
		String imageParentPath = Properties.RADARIMAGEROOTPATH + fileName.substring(0, 8);
		// 创建存储文件夹
		FileUnit.mkdirs(imageParentPath);
		return new File(imageParentPath + "/" + fileName + ".png");
	}

	/**
	 * 改变日历对象
	 * 
	 * @param date
	 * @return
	 */
	public static Date changeDate(Date date) {
		// 当前时间提前30分钟
		date = DateUnit.addDate(date, Calendar.MINUTE, -30);
		// 日历对象的分钟数
		int minute = DateUnit.get(date, Calendar.MINUTE);
		// 日历对象的分钟修改
		return DateUnit.addDate(date, Calendar.MINUTE, -(minute % 6));
	}

	/**
	 * 定时执行的任务方法 获取雷达气象图（全国）
	 */
	public static void executeJob(Date date) {
		// Date date = new Date();
		// 获取存储文件
		File file = getImgFile(date);
		System.out.println(file);
		// 得到请求编码参数
		String param = getRequestParam(date);
		try {
			/* 将网络资源地址传给,即赋值给url */
			URL url = new URL(
					"http://pi.weather.com.cn/i/product/pic/l/sevp_aoc_rdcp_sldas_ebref_achn_l88_pi_" + param + ".png");
			System.out.println(url.getPath());
			/* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 根据相应头信息判断响应信息是否为雷达图
			Map<String, List<String>> map = connection.getHeaderFields();
			String value = map.get("Content-Type").get(0);
			// 响应信息不是雷达图直接结束任务
			if (!"image/png".equals(value)) {
				return;
			}
			/*************************/
			DataInputStream in = new DataInputStream(connection.getInputStream());
			/* 此处也可用BufferedInputStream与BufferedOutputStream */
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			/* 将参数savePath，即将截取的图片的存储在本地地址赋值给out输出流所指定的地址 */
			byte[] buffer = new byte[4096];
			int count = 0;
			/* 将输入流以字节的形式读取并写入buffer中 */
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();/* 后面三行为关闭输入输出流以及网络资源的固定格式 */
			in.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
