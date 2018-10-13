package com.weather.unit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task {
	
	/**
	 * 定时执行的任务方法
	 * 获取雷达气象图（全国）
	 */
	public void executeJob() {
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		BufferedInputStream bufferedInputStream = null;
		OutputStream outputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		File file = new File("/radar map.png");
		try {
			// url 地址
			URL url = new URL("http://pi.weather.com.cn/i/product/pic/l/sevp_aoc_rdcp_sldas_ebref_achn_l88_pi_20181009074200001.png");
			// 获取连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接属性
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			// connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE
			// 5.0; Windows NT; DigExt)");
			 connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			 // 获取输入流,用作缓冲引子
			inputStream = connection.getInputStream();
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
				if(bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
				if(outputStream != null) {
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
