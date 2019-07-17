package com.kunteng.cyria.dashboard.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.domain.DataCell;
import com.kunteng.cyria.dashboard.domain.TempCSV;
import com.kunteng.cyria.dashboard.domain.TitleCell;
import com.kunteng.cyria.dashboard.repository.FileCSVRepository;
import com.kunteng.cyria.dashboard.repository.TempCSVRepository;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.kunteng.cyria.dashboard.utils.Utils;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import feign.Logger;

@Service
public class FileCSVServiceImpl implements FileCSVService {
	
	@Autowired
	private FileCSVRepository fileCSVRepository;
	
	@Autowired
	private TempCSVRepository tempCSVRepository;
	
	public  boolean isCsv(String fileName) {
		if(fileName.endsWith(".csv")) {
			return true;
		}else {
			return false;
		}
	}
	
	private String readCSV(String fileName, File filePath) {
		try {
			TempCSV tempCSV = new TempCSV();
			tempCSV.setFileName(fileName);
			
			DataInputStream in = new DataInputStream(new FileInputStream(filePath));
			CSVReader csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));
			
			ArrayList<TitleCell> titleHeader = new ArrayList<TitleCell>();
			String[] header = csvReader.readNext();
			for(int i=0; i < header.length; i++) {
				TitleCell title = new TitleCell();
				title.setTitle(header[i]);
				titleHeader.add(title);
			}
			tempCSV.setTitle(titleHeader);
			
			
			ArrayList< Map<String,String>> dataList = new ArrayList< Map<String,String>>();
			String[] str;
			while((str = csvReader.readNext())!= null) {
				Map<String,String> map = new HashMap<>();
				for(int i=0;i < str.length; i++) {
					map.put(header[i], str[i]);
				} 
				dataList.add(map);
			}
			tempCSV.setData(dataList);
			csvReader.close();
		
			tempCSVRepository.save(tempCSV);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	private String testCSV(String fileName) {
		
		return fileName;
	}
	
	
	public CommonResult uploadFileCSV(MultipartFile file) throws IOException {
		if(!isCsv(file.getOriginalFilename())) {
			return new CommonResult().customFailed("请选择CSV格式文件");
		}
		String fileName = file.getOriginalFilename();
		String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		
		System.out.println("fileName="+fileName);
		System.out.println("prefixName="+prefixName);
		System.out.println("suffixName="+suffixName);
		String newFileName = prefixName + "-" + System.currentTimeMillis()+ ".csv";
		File path = new File(Utils.getRootPath()+Utils.getCsvPath());
		if(!path.exists()) {
			path.mkdirs();
		}
		File restore =  new File(Utils.getRootPath()+Utils.getCsvPath()+ newFileName);
		if(!restore.exists()) {
			restore.createNewFile();
		} 
		
		file.transferTo(restore);
		String result = readCSV(fileName,restore);
		/*try {
			TempCSV tempCSV = new TempCSV();
			tempCSV.setFileName(prefixName);
			
			DataInputStream in = new DataInputStream(new FileInputStream(restore));
			CSVReader csvReader = new CSVReader(new InputStreamReader(in, "UTF-8"));
			
			ArrayList<TitleCell> titleHeader = new ArrayList<TitleCell>();
			String[] header = csvReader.readNext();
			for(int i=0; i < header.length; i++) {
				TitleCell title = new TitleCell();
				title.setTitle(header[i]);
				titleHeader.add(title);
			}
			tempCSV.setTitle(titleHeader);
			
			
			ArrayList< Map<String,String>> dataList = new ArrayList< Map<String,String>>();
			String[] str;
			while((str = csvReader.readNext())!= null) {
				Map<String,String> map = new HashMap<>();
				for(int i=0;i < str.length; i++) {
				//	DataCell data = new DataCell();
				//	data.setTitle(header[i]);
				//	data.setValue(str[i]);
				//	Map<String,String> map = new HashMap<>();
					map.put(header[i], str[i]);
				//	dataList.add(map);
				} 
				dataList.add(map);
			}
			tempCSV.setData(dataList);
			csvReader.close();
		
			tempCSVRepository.save(tempCSV);
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		return null;
		
	}

}
