package com.weather.unit;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 城市操作工具类
 * 
 * @author Administrator
 *
 */
public class UrlUnit {
	public static void main(String[] args) {
		String data = UrlUnit.getData("http://www.weather.com.cn/data/list3/city.xml");
		System.out.println(data);
	}

	/**
	 * 获取数据信息
	 * 
	 * @param httpUrl
	 * @return
	 */
	public static String getData(String httpUrl) {
		// logger.info("请求天气httpURl:" + httpUrl);
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		BufferedInputStream bufferedInputStream = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			URL url = new URL(httpUrl);
			// 获取连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接属性
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			// connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE
			// 5.0; Windows NT; DigExt)");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 获取输入流
			inputStream = connection.getInputStream();
			// 经过缓冲流存入StringBuffer
			bufferedInputStream = new BufferedInputStream(inputStream);
			byte[] buffer = new byte[1024];
			int length = -1;
			length = bufferedInputStream.read(buffer, 0, buffer.length);
			while (length != -1) {
				stringBuffer.append(new String(buffer, 0, length));
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
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();
	}
}
