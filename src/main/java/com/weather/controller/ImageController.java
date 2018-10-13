package com.weather.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.weather.common.Properties;
import com.weather.unit.FileUnit;

/**
 * 图片操作controller
 * @author Administrator
 *
 */
@RestController
public class ImageController {
	
	@Autowired
	private Gson gson;
	
	@RequestMapping("/download/{fileName}")
	public void download(@PathVariable("fileName")String fileName,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String contextPath = Properties.RADARIMAGEROOTPATH;
		//String fileName = request.getParameter("fileName");
		File file = new File(contextPath+fileName.substring(0, 8)+"/"+fileName+".png");
		if(file.exists()) {
			//设置MIME类型			
			response.setContentType("application/octet-stream");						
			//或者为response.setContentType("application/x-msdownload");						
			//设置头信息,设置文件下载时的默认文件名，同时解决中文名乱码问题			
			response.addHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes(), "ISO-8859-1"));
			InputStream inputStream = new FileInputStream(file);
			ServletOutputStream outputStream = response.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024];
			while ((count = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, count);
				outputStream.flush();
			}
			outputStream.close();
			inputStream.close();
		}
		
	}
	
	@RequestMapping("/images")
	@ResponseBody
	public String imageList(HttpServletRequest request,HttpServletResponse response) {
		// 获取当天的雷达文件
		String dateNum = new SimpleDateFormat("yyyyMMdd").format(new Date());
		// 获取雷达文件路径
		String contextPath = Properties.RADARIMAGEROOTPATH+dateNum;
		List<String> images = FileUnit.printFile(contextPath);
		return gson.toJson(images);
	}
}
