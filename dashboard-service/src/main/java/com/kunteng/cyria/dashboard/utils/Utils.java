package com.kunteng.cyria.dashboard.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

public class Utils {

	private static final String dashboardPath = "/static/upload/dashboards";
	
	private static final String imagesPath = "/static/upload/images";
	
	public static String getRootPath() throws FileNotFoundException {
		File rootPath = new File(ResourceUtils.getURL("classpath:").getPath());
		//File rootPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX);
		return rootPath.getAbsolutePath();
	}
	
	public static String getDashboardPath() {
		return dashboardPath;
	}
	
	public static String getImagesPath() {
		return imagesPath;
	}
	
	public static String hash(String password) {
		try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();

            for (byte b : result) {
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
	}
	
	public static String uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if(!targetFile.exists()) {
			targetFile.mkdirs();
		}
		
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
		
		return filePath + fileName;
	}
	
	public static String createImage(String srcPath, String id) throws Exception {
		if(srcPath == null) {
			return null;
		}
		
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] decoderBytes = decoder.decodeBuffer(srcPath.split(",")[1]);
		
		String filePath = getRootPath() + dashboardPath;
		String fileName = id + ".png";
		return uploadFile(decoderBytes,filePath, fileName);
	}
	
	public static Object uploadImage(String id, MultipartFile files) throws IllegalStateException, IOException {
		String hash = null;
		if(id.isEmpty()) {
			hash = "anony";
		}else {
			hash = id;
		}
		
		String fileName = files.getOriginalFilename();
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		String hashName = Utils.hash(fileName)+suffixName;
		
		if(suffixName.equalsIgnoreCase(".jpg") || suffixName.equalsIgnoreCase(".jpeg") || suffixName.equalsIgnoreCase(".png")) {
			File uploadPath = new File(getRootPath() + getImagesPath() + id + "/img/");
			File uploadFile = new File(getRootPath() + getImagesPath() + id + "/img/" ,hashName);
			if(!uploadPath.exists()) {
				uploadPath.mkdirs();
			}
			if(!uploadFile.exists()) {
				uploadFile.createNewFile();
			}
			
			files.transferTo(uploadFile);
			return getImagesPath()+ "/" + id + "/img/" + hashName;
		}else {
			return null;
		}
		
	}
}
