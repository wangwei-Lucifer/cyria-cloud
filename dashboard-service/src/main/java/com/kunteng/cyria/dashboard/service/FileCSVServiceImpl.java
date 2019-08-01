package com.kunteng.cyria.dashboard.service;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kunteng.cyria.dashboard.domain.FinalCSV;
import com.kunteng.cyria.dashboard.domain.RawCSV;
import com.kunteng.cyria.dashboard.domain.TitleCell;
import com.kunteng.cyria.dashboard.repository.FinalCSVRepository;
import com.kunteng.cyria.dashboard.repository.RawCSVRepository;
import com.kunteng.cyria.dashboard.utils.CityPos;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.kunteng.cyria.dashboard.utils.Utils;
import com.opencsv.CSVReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		Pattern pattern = Pattern.compile("(^[0-9]{4}[-/][0-9]{1,2}[-/][0-9]{1,2}$)|(^[0-9]{4}[-/][0-9]{1,2}$)");
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
		/*if(!isCsv(file.getOriginalFilename())) {
			return new CommonResult().customFailed("请选择CSV格式文件");
		}*/
		String fileName1 = file.getOriginalFilename();
		Decoder decoder = Base64.getDecoder();;
		byte[] bytes = decoder.decode(fileName1);
		String fileName = new String(bytes);

		if(!isCsv(fileName)) {
			return new CommonResult().customFailed("请选择CSV格式文件");
		}
		
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
		
		if(rawCSV == null) {
			return new CommonResult().customFailed("传参错误！");
		}
		
		RawCSV repoCSV = rawCSVRepository.findByHash(hash);
		if(repoCSV == null) {
			return new CommonResult().customFailed("数据库查询错误！");
		}
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
						case "date":
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
		/*if(!isCsv(file.getOriginalFilename())) {
			return new CommonResult().customFailed("请选择CSV格式文件");
		}*/

		String fileName1 = file.getOriginalFilename();
		Decoder decoder = Base64.getDecoder();;
		byte[] bytes = decoder.decode(fileName1);
		String fileName = new String(bytes);

		if(!isCsv(fileName)) {
			return new CommonResult().customFailed("请选择CSV格式文件");
		}
		
		System.out.println("fileName="+fileName);
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
		if(finalCSV == null) {
			return new CommonResult().customFailed("数据库查询失败！");
		}
		
		if(!isEqualTitle(repoCSV.getTitle(),finalCSV.getTitle())) {
			rawCSVRepository.deleteByHash(repoCSV.getHash());
			return new CommonResult().customFailed("表头不一致，无法更新数据");
		}
		
		ArrayList<Map<String,ArrayList<String>>> data = repoCSV.getData();
		ArrayList<Map<String,ArrayList<String>>> refeshData = new ArrayList<Map<String,ArrayList<String>>>();
		for(int i=0; i<data.size(); i++) {
			Map<String,ArrayList<String>> dataCell = new HashMap<String,ArrayList<String>>();
			for(String s: data.get(i).keySet()) {
				ArrayList<String> dataCellData = checkCellType(s,repoCSV,data.get(i).get(s).get(1));
                                if(dataCellData.size()!=0){dataCell.put(s,dataCellData);}
                                /* 将以下逻辑转化为checkCellType方法避免3层forloop
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
				}*/
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
	public ArrayList<String> checkCellType(String title,RawCSV repoCSV,String val){
	       ArrayList<String> dataCellData = new ArrayList<>();
				for(int j=0; j<repoCSV.getTitle().size(); j++) {
					if(title.equals(repoCSV.getTitle().get(j).getTitle())) {
						String titleType = repoCSV.getTitle().get(j).getTitleType();
						boolean result = false;
						switch(titleType) {
						case "number":
							result = isNumeric(val);
							break;
						case "position":
							result = isChinaProvince(val);
							break;
						case "data":
							result = isDateTime(val);
							break;
						case "text":
							result = true;
							break;
						}
						dataCellData.add(String.valueOf(result));
						dataCellData.add(val);
						//dataCell.put(s,dataCellData );
					}
				}
               return dataCellData;
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
	
	public CommonResult cancelCSV(String hash) {
		rawCSVRepository.deleteByHash(hash);
		return new CommonResult().success("取消成功");
	}
	public ArrayList<String> getCsvRaw(FinalCSV fv, String rowtabname) {
		ArrayList<String> strs = new ArrayList<String>();
                ArrayList<Map<String,ArrayList<String>>> data=fv.getData();
                for(Map<String,ArrayList<String>> mp : data){
                   ArrayList<String> slist=mp.get(rowtabname);
                   String val=slist.get(1);
                   System.out.println("Val:" + val);
                   strs.add(val);
                }
                return strs;
        }
	public String[] getCityPos(String s) {
		String [] posdata= CityPos.posdata;
		String[] ss=new String[2];
		ss[0]="0.0";
		ss[1]="0.0";
		for(int i=0;i<posdata.length/3;i++) {
			if(posdata[i*3].equals(s)) {
				ss[0]=posdata[i*3+1];
				ss[1]=posdata[i*3+2];
			}
		}
		return ss;
	}
	public String getJsonApiData(String groups,String values,String type,String suuid,String source){
               boolean manyparam=false;
		boolean manygroup=false;
		log.info("RequestParam groups:" + groups);
		log.info("RequestParam values:" + values);
		log.info("RequestParam type :" + type);
		log.info("RequestParam suuid:" + suuid);
		log.info("RequestParam source:" + source);

                String srcgroups = "";
		String srcvalues = "";
		try {
			srcgroups = new String(Base64.getDecoder().decode(groups), "utf8");
			srcvalues = new String(Base64.getDecoder().decode(values), "utf8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("RequestParam srcgroups:" + srcgroups);
		log.info("RequestParam srcvalues:" + srcvalues);
                FinalCSV fv = finalCSVRepository.findByHash(source);
                if(fv==null){
                   JSONObject jsob = new JSONObject();
                   jsob.put("statusCode", 1);
                   return jsob.toString();
                }
                ArrayList<TitleCell> list=fv.getTitle();
                for(TitleCell tv : list){
                  log.info("TV title:" + tv.getTitle());
                }
		if(srcvalues!=null) {
			if (srcvalues.split("-").length != 1) {
				manyparam=true;
			}
			if(type.equals("uuid6")) {
				manyparam=true;
			}
			if(type.equals("uuid7")) {
				manyparam=true;
			}
		}
		if(srcgroups!=null) {
			if (srcgroups.split("-").length != 1) {
				manygroup=true;
			}
			if(type.equals("uuid9")) {
				manygroup=true;
			}
		}
		System.out.println("RequestParam manygroup:" + manygroup);
		System.out.println("RequestParam manyparam:" + manyparam);
                if (manygroup==false) {
                  ArrayList<String> strs1 = getCsvRaw(fv, srcgroups);
			if (manyparam==false) {//如果manyparam为false的话则是 柱状图，折线区域图，堆叠柱状图，玫瑰图，省份分布图 中的一种以UUID进行区分
                  		ArrayList<String> strs2 = getCsvRaw(fv, srcvalues);
				log.info("RequestParam srcgroups:" + srcgroups);
				log.info("RequestParam srcvalues:" + srcvalues);
				JSONObject jsob = new JSONObject();
                                jsob.put("statusCode", 0);
				int len = strs1.size();
				if (len >= 12) {
					len = 12;
				}
				if (type.equals("uuid1")) {//柱状图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("x", strs1.get(i));
						jo.put("y", strs2.get(i));
						ja.add(jo);
					}
					data.put("statistics", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid2")) {//折线区域图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("x", strs1.get(i));
						jo.put("y", strs2.get(i));
						ja.add(jo);
					}
					data.put("statistics", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid3")) {//堆叠柱状图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("x", strs1.get(i));
						jo.put("y", strs2.get(i));
						ja.add(jo);
					}
					data.put("statistics", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid4")) {//玫瑰图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("x", strs1.get(i));
						jo.put("y", strs2.get(i));
						ja.add(jo);
					}
					data.put("statistics", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid5")) {//地图省份分布图1:1
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", strs1.get(i));
						jo.put("value", strs2.get(i));
						ja.add(jo);
					}
					data.put("china", ja);
					jsob.put("data", data);
				}  if (type.equals("uuid8")) {//地图分布散点图1:1的情况
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONArray ja = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", strs1.get(i));
						JSONArray joa= new JSONArray();
						String value=strs2.get(i);
						String pos[]=getCityPos(strs1.get(i));
						joa.add(Double.parseDouble(pos[0]));
						joa.add(Double.parseDouble(pos[1]));
						joa.add(Double.parseDouble(value));
						jo.put("value", joa);
						ja.add(jo);
					}
					data.put("china", ja);
					jsob.put("data", data);
				}
				return jsob.toString();
                  }else{//如果manyparam不为false的话则为折线堆叠图，雷达图，分布散点图其中一种以uuid进行区分
				String arrs[]=srcvalues.split("-");
				JSONObject jsob = new JSONObject();
                                jsob.put("statusCode", 0);
				int len = strs1.size();
				if (len >= 12) {
					len = 12;
				}
				ArrayList<String>[] strsarr = new ArrayList[arrs.length];
				for(int i=0;i<arrs.length;i++) {
					strsarr[i]= getCsvRaw(fv, arrs[i]);
				}
				if (type.equals("uuid6")) {//折线对叠图1:M的情况
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONObject dataseries= new JSONObject();
					JSONArray categories = new JSONArray();
					for (int i = 0; i < len; i++) {
						categories.add(strs1.get(i));
					}
					dataseries.put("categories", categories);
					JSONArray statistics= new JSONArray();
					for (int i = 0; i < arrs.length; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", arrs[i]);
						JSONArray ja = new JSONArray();
						for (String s : strsarr[i]) {
							ja.add(Integer.parseInt(s));
						}
						jo.put("data",ja);
						statistics.add(jo);
					}
					dataseries.put("statistics", statistics);
					data.put("dataseries", dataseries);
					jsob.put("data", data);
				}
				if (type.equals("uuid7")) {//雷达图1:M的情况
					jsob.put("code", 0);
					JSONObject data = new JSONObject();
					JSONObject dataseries= new JSONObject();
					JSONArray categories = new JSONArray();
					for (int i = 0; i < len; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", strs1.get(i));
						//jo.put("max",600);
						categories.add(jo);
					}
					dataseries.put("categories", categories);
					JSONArray statistics= new JSONArray();
					for (int i = 0; i < arrs.length; i++) {
						JSONObject jo = new JSONObject();
						jo.put("name", arrs[i]);
						JSONArray ja = new JSONArray();
						for (String s : strsarr[i]) {
							ja.add(Double.parseDouble(s));
						}
						jo.put("value",ja);
						statistics.add(jo);
					}
					dataseries.put("statistics", statistics);
					data.put("dataseries", dataseries);
					jsob.put("data", data);
				}
				return jsob.toString();
                  }
                }else{// 如果srcgroups长度不为1的话则是图表控件按图表控件标准进行返回即可
			String arrs[]=srcgroups.split("-");
			ArrayList<String>[] strsarr = new ArrayList[arrs.length];
			for(int i=0;i<arrs.length;i++) {
				strsarr[i]= getCsvRaw(fv, arrs[i]);
			}
			int len = strsarr[0].size();
			if (len >= 12) {
				len = 12;
			}
			JSONObject jsob = new JSONObject();
                        jsob.put("statusCode", 0);
			if (type.equals("uuid9")) {//表格M:0的情况
				jsob.put("status", 0);
				JSONObject data = new JSONObject();
				JSONArray columns= new JSONArray();
				//生成columns数据
				for(int i=0;i<arrs.length;i++) {
					JSONObject jo = new JSONObject();
					jo.put("name", arrs[i]);
					jo.put("id","vl"+i);
					columns.add(jo);
				}
				data.put("columns", columns);
				//生成row数据
				JSONArray rows = new JSONArray();
				for (int i = 0; i < len; i++) {
					JSONObject jo = new JSONObject();
					//fill jo by forloop
					for(int j=0;j<strsarr.length;j++) {
						String vlx=strsarr[j].get(i);
						jo.put("vl"+j, vlx);
					}
					rows.add(jo);
				}
				data.put("rows", rows);
				jsob.put("data", data);
			}
			return jsob.toString();
                }
        }
}
