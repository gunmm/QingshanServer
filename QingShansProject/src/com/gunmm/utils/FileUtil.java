package com.gunmm.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;


@SuppressWarnings("restriction")
public class FileUtil {

	/**
	 * 保存图片的base64位编码
	 * @param base64img
	 * @param session
	 * @return
	 */
	public static String uploadBase64Img(String base64img,String savePath){
		if(StringUtils.isNotBlank(base64img)){		
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				// Base64解码
				byte[] bytes = decoder.decodeBuffer(base64img);
				for (int i = 0; i < bytes.length; ++i) {
					if (bytes[i] < 0) {// 调整异常数据
						bytes[i] += 256;
					}
				}
				// 生成jpg图片
				String dirPath = savePath;
				SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
				String formatStr = formatter.format(new Date());
				String fileName = formatStr+".jpg";
				File dir = new File(dirPath);
				if (!dir.exists() && !dir.isDirectory()) {
					dir.mkdirs();
				}
				String imgFilePath = dirPath+fileName;
				OutputStream out = new FileOutputStream(imgFilePath);
				out.write(bytes);
				out.flush();
				out.close();
				return fileName;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else
			return null;
	}
}
