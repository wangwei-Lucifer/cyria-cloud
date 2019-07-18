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

import com.kunteng.cyria.dashboard.domain.FinalCSV;
import com.kunteng.cyria.dashboard.domain.RawCSV;
import com.kunteng.cyria.dashboard.domain.TitleCell;
import com.kunteng.cyria.dashboard.repository.FinalCSVRepository;
import com.kunteng.cyria.dashboard.repository.RawCSVRepository;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.kunteng.cyria.dashboard.utils.Utils;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import feign.Logger;

@Service
public class FileCSVServiceImpl implements FileCSVService {
	
	@Autowired
	private FinalCSVRepository finalCSVRepository;
	
	@Autowired
	private RawCSVRepository rawCSVRepository;
	
	public  boolean isCsv(String fileName) {
		if(fileName.endsWith(".csv")) {
			return true;
		}else {
			return false;
		}
	}
	
	private CommonResult readCSV(String fileName, File filePath) {
		try {
			RawCSV rawCSV = new RawCSV();
			rawCSV.setFileName(fileName);
			
			DataInputStream in = new DataInputStream(new FileInputStream(filePath));
			CSVReader csvReader = new CSVReader(new InputStreamReader(in, "GB2312"));
			
			ArrayList<TitleCell> titleHeader = new ArrayList<TitleCell>();
			String[] header = csvReader.readNext();
			for(int i=0; i < header.length; i++) {
				TitleCell title = new TitleCell();
				title.setTitle(header[i]);
				titleHeader.add(title);
			}
			rawCSV.setTitle(titleHeader);
			
			
			ArrayList< Map<String,String>> dataList = new ArrayList< Map<String,String>>();
			String[] str;
			while((str = csvReader.readNext())!= null) {
				Map<String,String> map = new HashMap<>();
				for(int i=0;i < str.length; i++) {
					map.put(header[i], str[i]);
				} 
				dataList.add(map);
			}
			rawCSV.setData(dataList);
			csvReader.close();
		
			RawCSV save = rawCSVRepository.save(rawCSV);
			
			return new CommonResult().success(rawCSV);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new CommonResult().failed();
	}
	
/*	private boolean titleListEqual(ArrayList<TitleCell> A1, ArrayList<TitleCell> A2) {
		if(A1.size() == A2.size()){
			for(int i =0 ; i < A1.size();i++) {
				if(!A1.get(i).getTitle().equals(A2.get(i).getTitle())) {
					return false;
				}
			}
			return true;
		}
		return false;
		
	}
	
	private CommonResult testCSV(String fileName) {
		FinalCSV finalCSV = finalCSVRepository.findByFileName(fileName);
		RawCSV rawCSV = rawCSVRepository.findByFileName(fileName);
		
		CommonResult result = new CommonResult();
		if(finalCSV.getFileName().equals(rawCSV.getFileName())) {
			if(titleListEqual(finalCSV.getTitle(),rawCSV.getTitle())) {
				result.setCode(1001);
				result.setMsg("该数据表已存在，要如何处理?");
				return result;
			}else {
				result.setCode(1002);
				result.setMsg("数据表名称冲突，请修改名称");
				return result;
			}
		}
		
		finalCSV.setFileName(rawCSV.getFileName());
		finalCSV.setTitle(rawCSV.getTitle());
		finalCSV.setData(rawCSV.getData());
		finalCSVRepository.save(rawCSV);
		rawCSVRepository.delete(id);
		return new CommonResult().success("上传成功！");
		
	}*/
	
	
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
		CommonResult result = readCSV(fileName,restore);
		return result;	
	}

}
