package com.kunteng.cyria.dashboard.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
	
	private static List<String> tableProvinces = new ArrayList<String>() {
			{
				add("北京");add("天津");add("上海");add("重庆");add("河北");add("河南");add("云南");add("辽宁");
				add("黑龙江");add("湖南");add("安徽");add("山东");add("新疆");add("江苏");add("浙江");add("江西");
				add("湖北");add("广西");add("甘肃");add("山西");add("内蒙古");add("陕西");add("吉林");add("福建");
				add("贵州");add("广东");add("青海");add("西藏");add("四川");add("宁夏");add("海南");add("台湾");
				add("香港");add("澳门");
			}
		};
	
	/**
	 * 匹配是否为数字 
	 * @param str 可能为大数字，使用BigDecimal
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
		//下面的正则把111.这种小数点在最后的判断为false
		// Pattern pattern = Pattern.compile.("-?[0-9]+\\.?[0-9]*"); 
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
		}catch(Exception e) {
			return false;
		}
		Matcher isNum = pattern.matcher(bigStr);
		if(!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	private static boolean isDateTime(String str) {
		Pattern pattern = Pattern.compile("^[0-9]{4}[-/ ](((0[13578]|(10|12))[-/ ](0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)[-/ ](0[1-9]|[1-2][0-9]|30)))$");
		Matcher isDate = pattern.matcher(str);
		if(!isDate.matches()) {
			return false;
		}
		return true;
	}
	
	private static boolean isChinaProvince(String str) {
		for(int i =0;i <tableProvinces.size(); i++) {
			if(tableProvinces.get(i).indexOf(str)!= -1) {
				return true;
			}
		}
		return false;
	}
	
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
			
			
			ArrayList<Map<String,ArrayList<String>>> dataList = new ArrayList< Map<String,ArrayList<String>>>();
			String[] str; 
			while((str = csvReader.readNext())!= null) {
				Map<String,ArrayList<String>> map = new HashMap<>();
				for(int i=0;i < str.length; i++) {
					ArrayList<String> dataSet = new ArrayList<>();
					dataSet.add("true");
					dataSet.add(str[i]);
					map.put(header[i], dataSet);
				} 
				dataList.add(map);
			}
			rawCSV.setData(dataList);
			csvReader.close();
		
			RawCSV save = rawCSVRepository.save(rawCSV);
			
			return new CommonResult().success(save);
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

	public CommonResult getCSVList(Map<String,Object> map) {
		int size = map.size();
		int page = 1;
		int limit = 20;
		String title = "";
		
		if(size != 0) {
			for(String key: map.keySet()) {
				if (key.equalsIgnoreCase("page")) {
					page = Integer.decode(map.get(key).toString());
				}
				
				if(key.equalsIgnoreCase("limit")) {
					limit = Integer.decode(map.get(key).toString());
				}
				
				if(key.equalsIgnoreCase("title")) {
					title = (String) map.get(key);
				}
			}
		}

		System.out.printf("page =%d, limit =%d, title= %s\n", page,limit,title);
		Sort sort = new Sort(Sort.Direction.DESC,"timestamp");
		PageRequest pageRequest = new PageRequest(page-1, limit, sort);
		List<FinalCSV> finalCSV = null;
		long sum = 0;
		
		System.out.println("run here!");
	/*	if("".equals(title)) {
			dashboard = finalCSVRepository.findAll(pageRequest);
			sum = finalCSVRepository.count();
		}else {
			dashboard = finalCSVRepository.findByTitle(title);
			sum = finalCSVRepository.countByTitle(title);
		}

		Map<String,Object> result = new HashMap<>();
		result.put("items", finalCSV);
		result.put("total", sum);
		return new CommonResult().success(result);*/
		return null;
	}
	
	public CommonResult saveCSVTitle(String hash, RawCSV rawCSV) {
		System.out.println(rawCSV.getFileName());
		System.out.println(rawCSV.getHash());
		System.out.println(rawCSV.getTimestamp());
		for(int i =0; i< rawCSV.getTitle().size();i++) {
			System.out.println(rawCSV.getTitle().get(i).getTitle());
			System.out.println(rawCSV.getTitle().get(i).getTitleType());
		}
		
		RawCSV repoCSV = rawCSVRepository.findByHash(hash);
		repoCSV.setFileName(rawCSV.getFileName());
		repoCSV.setTitle(rawCSV.getTitle());
		
		ArrayList<Map<String,ArrayList<String>>> data = repoCSV.getData();
		ArrayList<Map<String,ArrayList<String>>> refeshData = new ArrayList<Map<String,ArrayList<String>>>();
	//	Map<String,ArrayList<String>> dataCell = new HashMap<String,ArrayList<String>>();
	//	ArrayList<String> dataCellData = new ArrayList<>();
		
		for(int i=0; i<data.size(); i++) {
			Map<String,ArrayList<String>> dataCell = new HashMap<String,ArrayList<String>>();
			for(String s: data.get(i).keySet()) {
				ArrayList<String> dataCellData = new ArrayList<>();
				for(int j=0; j<repoCSV.getTitle().size(); j++) {
					if(s.equals(repoCSV.getTitle().get(j).getTitle())) {
						System.out.printf("%s,%s\n",s,repoCSV.getTitle().get(j).getTitle());
					//	ArrayList<String> dataCellData = new ArrayList<>();
						String titleType = repoCSV.getTitle().get(j).getTitleType();
						boolean result = false;
						switch(titleType) {
						case "number":
							result = isNumeric(data.get(i).get(s).get(1));
							break;
						case "position":
							result = isChinaProvince(data.get(i).get(s).get(1));
							break;
						case "data":
							result = isDateTime(data.get(i).get(s).get(1));
							break;
						case "text":
							result = true;
							break;
						}
						dataCellData.add(String.valueOf(result));
						dataCellData.add(data.get(i).get(s).get(1));
						dataCell.put(s,dataCellData );
					}
				}
			}
			refeshData.add(dataCell);
		//	refeshData.add(i, dataCell);
		}
		
		repoCSV.setData(refeshData);
		
		RawCSV saveCSV = rawCSVRepository.save(repoCSV);
		
		
		return new CommonResult().success(saveCSV);
	}
}
