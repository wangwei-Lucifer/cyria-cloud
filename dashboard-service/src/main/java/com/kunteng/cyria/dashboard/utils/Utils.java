package com.kunteng.cyria.dashboard.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

public class Utils {
	private static final String rootPath ="/app/static";

	private static final String dashboardPath = "/upload/dashboards/";
	
	private static final String imagesPath = "/upload/images/";
	
	public static String getRootPath(){
	/*	File rootPath = new File(ResourceUtils.getURL("classpath:").getPath());
		return rootPath.getAbsolutePath();*/
		return rootPath;
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
		File targetFile = new File(getRootPath() + filePath);
		if(!targetFile.exists()) {
			targetFile.mkdirs();
		}
		
		FileOutputStream out = new FileOutputStream(getRootPath()  +filePath + fileName);
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
		
		String filePath = getDashboardPath();
		String fileName = id + ".png";
		return uploadFile(decoderBytes,filePath, fileName);
	}
	
	public static Object uploadImage(String id, MultipartFile file) throws IllegalStateException, IOException, NullPointerException{
		String hash = null;
		if(id.isEmpty()) {
			hash = "anony";
		}else {
			hash = id;
		}
		
		System.out.println("id="+id);
		if(!file.isEmpty()) {
			try {
				System.out.println("type="+file.getContentType());
				System.out.println("name="+file.getName());
				System.out.println("empty="+file.isEmpty());
				System.out.println("size="+file.getBytes());
			}catch(NullPointerException e) {
				e.printStackTrace();
				return e.getMessage();
			}
			
			String fileName = file.getOriginalFilename();
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
				
				file.transferTo(uploadFile);
				return getImagesPath() + id + "/img/" + hashName;
			}else {
				return null;
			}
		}else {
			System.out.println("files is empty!");
			return null;
		}
		
	}
}
