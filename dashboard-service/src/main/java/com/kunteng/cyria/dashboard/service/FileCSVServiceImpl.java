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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.domain.FinalCSV;
import com.kunteng.cyria.dashboard.domain.RawCSV;
import com.kunteng.cyria.dashboard.domain.Template;
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
		if(str.isEmpty()) {
			return false;
		}else {
			for(int i =0;i <tableProvinces.size(); i++) {
				if(tableProvinces.get(i).indexOf(str)!= -1) {
					return true;
				}
			}
			return false;
		}
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
		CommonResult result = readCSV(prefixName,restore);
		return result;	
	}
	
	private static boolean isContainTitle(String title, ArrayList<TitleCell> titleList) {
		List<String> list = new ArrayList<String>();
		for(int i=0;i < titleList.size();i++) {
			list.add(titleList.get(i).getTitle());
		}
		return list.contains(title);
	}
	
	private static boolean isContailFileName(String fileName, List<FinalCSV> csvList) {
		List<String> list = new ArrayList<String>();
		for(int i=0;i<csvList.size();i++) {
			list.add(csvList.get(i).getFileName());
		}
		return list.contains(fileName);
	}

	public CommonResult getCSVList(Map<String,Object> map) {
		int size = map.size();
		int page = 1;
		int limit = 10;
		String fileName = "";
		
		if(size != 0) {
			for(String key: map.keySet()) {
				if (key.equalsIgnoreCase("page")) {
					page = Integer.decode(map.get(key).toString());
				}
				
				if(key.equalsIgnoreCase("limit")) {
					limit = Integer.decode(map.get(key).toString());
				}
				
				if(key.equalsIgnoreCase("fileName")) {
					fileName = (String) map.get(key);
				}
			}
		}

		System.out.printf("page =%d, limit =%d, fileName= %s\n", page,limit,fileName);
		Sort sort = new Sort(Sort.Direction.DESC,"timestamp");
		PageRequest pageRequest = new PageRequest(page-1, limit, sort);
	//	Page<FinalCSV> finalCSV = finalCSVRepository.findAll(pageRequest);
		long sum = 0;
	
		List<FinalCSV> filterCSV = new ArrayList<>();
		if("".equals(fileName)) {
			Page<FinalCSV> finalCSV = finalCSVRepository.findAll(pageRequest);
			sum = finalCSVRepository.count();
			for(int i=0;i<finalCSV.getContent().size();i++) {
				filterCSV.add(finalCSV.getContent().get(i));
			}
		}else {
			List<FinalCSV> total = finalCSVRepository.findAll(sort);
			if(isContailFileName(fileName,total)) {
				FinalCSV tmp = finalCSVRepository.findByFileName(fileName);
				sum =1;
				filterCSV.add(tmp);
			}else {
				List<FinalCSV> save = new ArrayList<>();
				for(int i=0;i < total.size();i++) {
					if(isContainTitle(fileName, total.get(i).getTitle())) {
						save.add(total.get(i));
						sum++;
					}
				}
				for(int i=0; i<limit && i<sum;i++) {
					filterCSV.add(save.get((page-1)*limit+i));
				}
			}
		}
		for(int i=0;i<filterCSV.size();i++) {
			filterCSV.get(i).setData(null);
		}
		
		Map<String,Object> result = new HashMap<>();
		result.put("items", filterCSV);
		result.put("total", sum);
		return new CommonResult().success(result);
	}
	
	public CommonResult viewCSVByHash(String hash) {
		if(hash == null || "".equals(hash) || hash.length()<=0 || hash.isEmpty()) {
			return new CommonResult().customFailed("hash传参错误！");
		}
		
		FinalCSV finalCSV = finalCSVRepository.findByHash(hash);
		return new CommonResult().success(finalCSV);
	}
	
	public CommonResult deleteCSVByHash(String hash) {
		if(hash == null || "".equals(hash) || hash.length()<=0 || hash.isEmpty()) {
			return new CommonResult().customFailed("hash传参错误！");
		}
		
		finalCSVRepository.deleteByHash(hash);
		return new CommonResult().success("删除成功！");
		
	}
	
	private static boolean isAllTrue(RawCSV rawCSV) {
		ArrayList<Map<String,ArrayList<String>>> data = rawCSV.getData();
		for(int i=0; i<data.size();i++) {
			for(String s: data.get(i).keySet()) {
				if(data.get(i).get(s).get(0).equals("false")){
					return false;
				}
			}
		}
		return true;
	}
	
	public CommonResult saveCSVTitle(String hash, RawCSV rawCSV) {
		if(hash == null || "".equals(hash) || hash.length()<=0 || hash.isEmpty()) {
			return new CommonResult().customFailed("hash传参错误！");
		}
		
		RawCSV repoCSV = rawCSVRepository.findByHash(hash);
		repoCSV.setFileName(rawCSV.getFileName());
		repoCSV.setTitle(rawCSV.getTitle());
		
		ArrayList<Map<String,ArrayList<String>>> data = repoCSV.getData();
		ArrayList<Map<String,ArrayList<String>>> refeshData = new ArrayList<Map<String,ArrayList<String>>>();
		
		for(int i=0; i<data.size(); i++) {
			Map<String,ArrayList<String>> dataCell = new HashMap<String,ArrayList<String>>();
			for(String s: data.get(i).keySet()) {
				ArrayList<String> dataCellData = new ArrayList<>();
				for(int j=0; j<repoCSV.getTitle().size(); j++) {
					if(s.equals(repoCSV.getTitle().get(j).getTitle())) {
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
	//	RawCSV saveCSV = rawCSVRepository.save(repoCSV);
		boolean flag = isAllTrue(repoCSV);
		if(flag) {
			FinalCSV tmpCSV = finalCSVRepository.findByFileName(repoCSV.getFileName());
			if(tmpCSV == null || tmpCSV.getHash() == null || "".equals(tmpCSV.getHash()) || tmpCSV.getHash().length()<=0 || tmpCSV.getHash().isEmpty()){
				FinalCSV finalCSV = new FinalCSV();
				finalCSV.setFileName(repoCSV.getFileName());
				finalCSV.setHash(repoCSV.getHash());
				finalCSV.setTitle(repoCSV.getTitle());
				finalCSV.setTimestamp(repoCSV.getTimestamp());
				finalCSV.setData(repoCSV.getData());
				FinalCSV finalCSVRep = finalCSVRepository.save(finalCSV);
				rawCSVRepository.deleteByHash(repoCSV.getHash());
				return new CommonResult().success("保存成功！");
			}else {
				return new CommonResult().customFailed(1000, "该表格名称已存在");
			}
		}else {
			rawCSVRepository.deleteByHash(repoCSV.getHash());
			CommonResult result = new CommonResult();
			result.setCode(0);
			result.setStatusCode(1001);
			result.setData(repoCSV);
			return result;
		}
	}
	
	private static boolean isEqualTitle(ArrayList<TitleCell> A1, ArrayList<TitleCell> A2) {
		if(A1.size() != A2.size()) {
			return false;
		}
		List<String> list1 = new ArrayList<>();
		List<String> list2 = new ArrayList<>();
		
		for(int i=0;i<A1.size();i++) {
			list1.add(A1.get(i).getTitle());
			list2.add(A2.get(i).getTitle());
		}
		
		Collections.sort(list1);
		Collections.sort(list2);
		return list1.equals(list2);
	}
	
	public CommonResult updateCSVData(String hash, MultipartFile file) throws IOException {
		if(!isCsv(file.getOriginalFilename())) {
			return new CommonResult().customFailed("请选择CSV格式文件");
		}
		String fileName = file.getOriginalFilename();
		String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
	//	String suffixName = fileName.substring(fileName.lastIndexOf("."));
		
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
		
		RawCSV rawCSV = new RawCSV();
		rawCSV.setFileName(prefixName);
		
		DataInputStream in = new DataInputStream(new FileInputStream(restore));
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
	
		RawCSV repoCSV = rawCSVRepository.save(rawCSV);
		FinalCSV finalCSV = finalCSVRepository.findByHash(hash);
		
		if(!isEqualTitle(repoCSV.getTitle(),finalCSV.getTitle())) {
			rawCSVRepository.deleteByHash(repoCSV.getHash());
			return new CommonResult().customFailed("表头不一致，无法更新数据");
		}
		
		ArrayList<Map<String,ArrayList<String>>> data = repoCSV.getData();
		ArrayList<Map<String,ArrayList<String>>> refeshData = new ArrayList<Map<String,ArrayList<String>>>();
		for(int i=0; i<data.size(); i++) {
			Map<String,ArrayList<String>> dataCell = new HashMap<String,ArrayList<String>>();
			for(String s: data.get(i).keySet()) {
				ArrayList<String> dataCellData = new ArrayList<>();
				for(int j=0; j<repoCSV.getTitle().size(); j++) {
					if(s.equals(repoCSV.getTitle().get(j).getTitle())) {
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
		}
		repoCSV.setData(refeshData);
		if(isAllTrue(repoCSV)) {
			finalCSV.setData(repoCSV.getData());
			finalCSV.setTimestamp(repoCSV.getTimestamp());
			FinalCSV finalCSVRep = finalCSVRepository.save(finalCSV);
			rawCSVRepository.deleteByHash(repoCSV.getHash());
			return new CommonResult().success("保存成功！");
		}else {
			rawCSVRepository.deleteByHash(repoCSV.getHash());
			return new CommonResult().success(repoCSV);
		}	
	}
	
	public CommonResult getTitleList() {
		Sort sort = new Sort(Sort.Direction.DESC,"timestamp");
		List<FinalCSV> list = finalCSVRepository.findAll(sort);
		List<Map<String,Object>> result = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			Map<String,Object> map = new HashMap<>();
			map.put("name", list.get(i).getFileName());
			map.put("hash", list.get(i).getHash());
			map.put("title", list.get(i).getTitle());
			result.add(map);
		}
		return new CommonResult().success(result);
	}
}
