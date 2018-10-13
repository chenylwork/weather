package com.weather.task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.weather.common.Properties;
import com.weather.unit.DateUnit;
import com.weather.unit.FileUnit;

/**
 * 定时获取中国天气网雷达数据（全国）
 * 
 * @author Administrator
 *
 */
public class RadarImgTask {

	/**
	 * 定时执行的任务方法
	 */
	public void job() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				executeJob();
			}
		};
		new Thread(runnable).start();
	}


	/**
	 * 生成雷达图像获取时间戳编码，气象网的雷达图每6分钟产生一张雷达图
	 * 获取当前时间的雷达图格式
	 * 20181013000600001
	 * 2018 10 13 00 06 00 001
	 *  年         月     日     时     分     秒
	 * 当前编码获取的雷达图对应的时间为
	 * 2018年10月13日08时06分00秒的雷达图
	 * 
	 * 获取的时间编码。
	 * 为当前时间的小时数减去8
	 * 当前时间的分钟数减去分钟数除以6的余数
	 * @return
	 */
	public String getRequestParam(Date date) {
		// 改变日历对象
		date = changeDate(date);
		// 日历对象的小时减8
		date = DateUnit.addDate(date, Calendar.HOUR_OF_DAY, -8);
		String dateNum = DateUnit.parseString(date, "yyyyMMddHHmm");
		return dateNum+"00001";
	}
	/**
	 * 网络获取的文件本地存储文件对象
	 * 
	 * @param nowDateNum
	 *            时间戳对应时间码 201809101212 年月日时分
	 * @return
	 */
	public File getImgFile(Date date) {
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
	 * @param date
	 * @return
	 */
	public static Date changeDate(Date date) {
		// 当前时间提前30分钟
		date = DateUnit.addDate(date, Calendar.MINUTE, -30);
		// 日历对象的分钟数
		int minute = DateUnit.get(date, Calendar.MINUTE);
		// 日历对象的分钟修改
		return DateUnit.addDate(date, Calendar.MINUTE, -(minute%6));
	}

	/**
	 * 定时执行的任务方法 获取雷达气象图（全国）
	 */
	public void executeJob() {
		Date date = new Date();
		// 获取存储文件
		File file = getImgFile(date);
		// 得到请求编码参数
		String param = getRequestParam(date);
		try {
			/* 将网络资源地址传给,即赋值给url */
			URL url = new URL("http://pi.weather.com.cn/i/product/pic/l/sevp_aoc_rdcp_sldas_ebref_achn_l88_pi_"
					+ param + ".png");
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

	/**
	 * 定时执行的任务方法 获取雷达气象图（全国） 错误需要修改 00001
	 */
	public void executeJob2() {
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		BufferedInputStream bufferedInputStream = null;
		OutputStream outputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		File file = new File("D:/radar map.png");
		try {
			// url 地址
			// 获取连接
			URL url = new URL("http://pi.weather.com.cn/i/product/pic/l/sevp_aoc_rdcp_sldas_ebref_achn_l88_pi_"
					+ getRequestParam(new Date()) + ".png");
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接属性
			connection.setRequestMethod("GET");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("contentType", "UTF-8");
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			// 获取输入流,用作缓冲引子
			connection.connect();
			inputStream = connection.getInputStream();
			String contentEncoding = connection.getContentEncoding();
			if ("gzip".equals(contentEncoding)) {
				inputStream = new GZIPInputStream(inputStream);
			}
			bufferedInputStream = new BufferedInputStream(inputStream);
			byte[] buffer = new byte[1024];
			int length = -1;
			// 读取规定大小数据
			length = bufferedInputStream.read(buffer, 0, buffer.length);
			// 获取输出流，作为输出缓冲的引子
			outputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			while (length != -1) {
				// 写入输出了
				bufferedOutputStream.write(buffer);
				// 重新读取长度
				length = bufferedInputStream.read(buffer, 0, buffer.length);
			}
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
