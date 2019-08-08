package com.kunteng.cyria.dashboard.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.domain.Config;
import com.kunteng.cyria.dashboard.domain.Dashboard;
import com.kunteng.cyria.dashboard.domain.Published;
import com.kunteng.cyria.dashboard.domain.Template;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.repository.DashboardRepository;
import com.kunteng.cyria.dashboard.repository.PublishedRepository;
import com.kunteng.cyria.dashboard.repository.TemplateRepository;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.kunteng.cyria.dashboard.utils.Utils;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class DashboardServiceImpl implements DashboardService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DashboardRepository dashboardRepository;
	
	@Autowired
	private TemplateRepository templateRepository;
	
	@Autowired
	private PublishedRepository publishedRepository;

	public CommonResult getAllDashboard(String user,  Map<String,Object> map) {
		int size = map.size();
		int page = 1;
		int limit = 20;
		String title = "";
		String status = "";
		String project = "";
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
				if(key.equalsIgnoreCase("status")) {
					status = (String) map.get(key);
				}
				
				if(key.equalsIgnoreCase("project")) {
					project = (String) map.get(key);
				}
			}
		}

		System.out.printf("user=%s, page =%d, limit =%d, title= %s, status = %s， project = %s\n",user, page,limit,title, status, project);
		Sort sort = new Sort(Sort.Direction.DESC,"timestamp");
		PageRequest pageRequest = new PageRequest(page-1, limit, sort);
		List<Dashboard> dashboard = null;
		long sum = 0;
		if(project.equals("all")) {
			if(("".equals(title)) && ("".equals(status))) {
				dashboard = dashboardRepository.findByUser(user, pageRequest);
				sum = dashboardRepository.countByUser(user);
			}else if("".equals(title)) {
				dashboard = dashboardRepository.findByUserAndStatus(user, status, pageRequest);
				sum = dashboardRepository.countByUserAndStatus(user, status);
			}else if("".equals(status)) {
				dashboard = dashboardRepository.findByUserAndTitle(user, title, pageRequest);
				sum = dashboardRepository.countByUserAndTitle(user, title);
			}else {
				dashboard = dashboardRepository.findByUserAndTitleAndStatus(user, title, status, pageRequest);
				sum = dashboardRepository.countByUserAndTitleAndStatus(user, title, status);
			}
		}else {
			System.out.println("run here!");
			if(("".equals(title)) && ("".equals(status))) {
				dashboard = dashboardRepository.findByUserAndProject(user, project, pageRequest);
				sum = dashboardRepository.countByUserAndProject(user, project);
			}else if("".equals(title)) {
				dashboard = dashboardRepository.findByUserAndStatusAndProject(user, status, project, pageRequest);
				sum = dashboardRepository.countByUserAndStatusAndProject(user, status, project);
			}else if("".equals(status)) {
				dashboard = dashboardRepository.findByUserAndTitleAndProject(user, title, project,pageRequest);
				sum = dashboardRepository.countByUserAndTitleAndProject(user, title, project);
			}else {
				dashboard = dashboardRepository.findByUserAndTitleAndStatusAndProject(user, title, status, project, pageRequest);
				sum = dashboardRepository.countByUserAndTitleAndStatusAndProject(user, title, status, project);
			}
		}

		Map<String,Object> result = new HashMap<>();
		result.put("items", dashboard);
		result.put("total", sum);
		return new CommonResult().success(result);
	}

	public CommonResult getDashboardById(String id) {
		Dashboard dashboard =  dashboardRepository.findByHash(id);
		if(dashboard == null) {
			return new CommonResult().customFailed("查询数据库失败！");
		}
		return new CommonResult().success(dashboard);
	}
	
	public CommonResult createNewDashboard(String id, Translation translation){
		log.info("translation=" + translation.toString());
		log.info("project = "+ translation.getProject());
		if(StringUtils.isBlank(translation.getName())) {
			return new CommonResult().customFailed("大屏名称不能为空！");
		}

		if(!translation.getIsTemplate()) {
			Dashboard dashboard = new Dashboard();
			
			dashboard.getConfig().setTitle(translation.getName()); 
			dashboard.getConfig().setAbout(translation.getAbout());
			if(!translation.getTemplate().equals("") && !translation.getTemplate().equals("blank")) {
				if(translation.getMode().equals("clone")) {
					Dashboard db = dashboardRepository.findByHash(translation.getTemplate());
					if(!db.getHash().equals("")) {
						dashboard.getConfig().setWidth(db.getConfig().getWidth());
						dashboard.getConfig().setHeight(db.getConfig().getHeight());
						dashboard.getConfig().setZoom(db.getConfig().getZoom());
						dashboard.getConfig().setBackgroundColor(db.getConfig().getBackgroundColor());
						dashboard.getConfig().setBackPic(db.getConfig().getBackPic());
						dashboard.getConfig().setColors(db.getConfig().getColors());
						dashboard.setWidget(db.getWidget());
						dashboard.getConfig().setPage(true);
						dashboard.setProject(db.getProject());
					}
				} else {
					Template template = templateRepository.findByHash(translation.getTemplate());
					if(!template.getHash().equals("")) {
						dashboard.getConfig().setWidth(template.getConfig().getWidth());
						dashboard.getConfig().setHeight(template.getConfig().getHeight());
						dashboard.getConfig().setZoom(template.getConfig().getZoom());
						dashboard.getConfig().setBackgroundColor(template.getConfig().getBackgroundColor());
						dashboard.getConfig().setBackPic(template.getConfig().getBackPic());
						dashboard.getConfig().setColors(template.getConfig().getColors());
						dashboard.setWidget(template.getWidget());
						dashboard.getConfig().setPage(true);
					}
					dashboard.setProject(translation.getProject());
				}
			}else {
				dashboard.setProject(translation.getProject());
			}
			dashboard.getConfig().setTimestamp(new Date().getTime());
			dashboard.getPublish().setStatus("unpublish");
			dashboard.getPublish().setTimestamp(new Date().getTime());
			dashboard.setUser(id);

			dashboardRepository.save(dashboard);
			String hash = dashboard.getHash();
			return new CommonResult().customHash(hash);
		}
		return new CommonResult().failed();
	}
	
	public CommonResult deleteDashboardByHash(String id) {
		String result =  dashboardRepository.deleteByHash(id);
		return new CommonResult().success(result);
	}

	public CommonResult publishDashboardById(String id, Object option) {
		System.out.println("option="+option.toString());
		System.out.println("id="+id);
		
		String[] temp;
		temp = option.toString().replaceAll("[\\{\\}]","").split("=");
		String opt = temp[1];
		System.out.println("opt="+opt);
		CommonResult result = new CommonResult();
		Dashboard dashboard = dashboardRepository.findByHash(id);
		if(dashboard == null) {
			return new CommonResult().customFailed("查询数据库失败！");
		}

		dashboard.getPublish().setStatus(opt);
		dashboard.getPublish().setTimestamp(new Date().getTime());
		System.out.println("hash1="+ dashboard.getPublish().getHash());
		if(opt.equals("unpublish")){
			publishedRepository.deleteByHash(dashboard.getPublish().getHash());
			System.out.println("hash2="+ dashboard.getPublish().getHash());
			dashboard.getPublish().setHash("");
			result.setCode(0);
			result.setMsg("停止发布成功");
			result.setData(null);
		}
		
		if(opt.equals("published")){
			System.out.println("XXXXXXXXXX");
			Published published = new Published();
			dashboard.getPublish().setHash(published.getHash());
			published.setConfig(dashboard.getConfig());
			published.setWidget(dashboard.getWidget());
			published.getPublish().setTimestamp(new Date().getTime());
			Published publish =publishedRepository.save(published);
			result.setCode(0);
			//result.setData(publish);
			result.setMsg("发布成功");
		}
		
		if(opt.equals("republish")){
			dashboard.getPublish().setStatus("published");
			Published published = publishedRepository.findByHash(dashboard.getPublish().getHash());
			published.setConfig(dashboard.getConfig());
			published.setWidget(dashboard.getWidget());
			published.getPublish().setTimestamp(new Date().getTime());
			Published publish = publishedRepository.save(published);
			result.setCode(0);
		//	result.setData(publish);
			result.setMsg("重新发布成功");
		}
		dashboardRepository.save(dashboard);
		return result;
	}

	public CommonResult updateDashboardById(String id, String db) throws Exception{
	//	JSONObject jso = JSONObject.fromObject(db);
		JSONObject jso = JSONObject.parseObject(db);
		Dashboard dashboard = dashboardRepository.findByHash(id);
		if(dashboard == null) {
			return new CommonResult().customFailed("查询数据库失败！");
		}
		
	/*	if(jso.has("config")) {
			JSONObject configObject = jso.getJSONObject("config");
			Config config = (Config)JSONObject.toBean(configObject, Config.class);
			dashboard.setConfig(config);
			dashboard.getConfig().setTimestamp(new Date().getTime());
		}
		if(jso.has("widget")) {
			JSONArray widget = jso.getJSONArray("widget");
			dashboard.setWidget(widget);
			dashboard.getConfig().setTimestamp(new Date().getTime());
		}
		if(jso.has("imgData")) {
			String imgUrl = Utils.createImage(jso.getString("imgData"),id);
	//		dashboard.setImgData(jso.getString("imgData"));
			dashboard.setImgUrl(imgUrl);
			dashboard.getConfig().setTimestamp(new Date().getTime());
		}*/
		
		if(jso.containsKey("config")) {
			JSONObject configObject = jso.getJSONObject("config");
			Config config = JSONObject.toJavaObject(configObject, Config.class);
			dashboard.setConfig(config);
			dashboard.getConfig().setTimestamp(new Date().getTime());
		}
		if(jso.containsKey("widget")) {
			JSONArray widget = jso.getJSONArray("widget");
			dashboard.setWidget(widget);
			dashboard.getConfig().setTimestamp(new Date().getTime());
		}
		if(jso.containsKey("imgData")) {
			String imgUrl = Utils.createImage(jso.getString("imgData"),id);
	//		dashboard.setImgData(jso.getString("imgData"));
			dashboard.setImgUrl(imgUrl);
			dashboard.getConfig().setTimestamp(new Date().getTime());
		}
		
		Dashboard result = dashboardRepository.save(dashboard);
		return new CommonResult().success(result);
	}
	
	public CommonResult getPublishedById(String id) {
		Published published = publishedRepository.findByHash(id);
		if(published == null) {
			return new CommonResult().customFailed("查询数据库失败！");
		}
		return new CommonResult().success(published);
	}

	@Override
	public CommonResult uploadImage(String id, MultipartFile file) throws IllegalStateException, IOException {
		return new CommonResult().customUpload((String)Utils.uploadImage(id, file));
	}
	
	private String cmdExec(String cmd, String path) {
		System.out.println("cmd="+cmd);
		System.out.println("path="+path);
		StringBuilder result = new StringBuilder();
		Process process = null;
		BufferedReader bufIn = null;
		BufferedReader bufError = null;
		String s = null;
		String line = null;
		int status = 0;
		ProcessBuilder pb = new ProcessBuilder(cmd, path);
		pb.directory(new File(Utils.getRootPath()));
		
		try {
			process = pb.start();
			try {
				status = process.waitFor();
			}catch(InterruptedException e) {
				e.printStackTrace();
				s = e.getMessage();
			}
		}catch(IOException e) {
			e.printStackTrace();
			s = e.getMessage();
		}   
		
		try {
			if(status == 0) {
				bufIn = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
				while((line = bufIn.readLine())!= null) {
					result.append(line).append('\n');
				}
				System.out.println("result1="+result.toString());
				return result.toString();
			}else {
				bufError = new BufferedReader(new InputStreamReader(process.getErrorStream(),"UTF-8"));
				while((line = bufError.readLine())!= null) {
					result.append(line).append('\n');
				}
				System.out.println("result2="+result.toString());
				return result.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			s = e.getMessage();
		}finally {
			if(bufIn != null) {
				try {
					bufIn.close();
				}catch(Exception e) {
					e.printStackTrace();
					s = e.getMessage();
				}
			}
			if(bufError != null) {
				try {
				    bufError.close();
				}catch(Exception e) {
					e.printStackTrace();
					s = e.getMessage();
				}
			}
		}
		System.out.println("s="+s);
		return s;
	}
	
	public CommonResult downloadDashboardById(String id) throws IOException {
		Published published = publishedRepository.findByHash(id);
		if(published == null) {
			return new CommonResult().customFailed("查询数据库失败！");
		}
		
		if(!published.getHash().equals("")) {
			String path = Utils.getRootPath() + "/public/files/";
			String fileName = id + ".json";
			File downPath = new File(path);
			File downFile = new File(path ,fileName);
			if(!downPath.exists()) {
				downPath.mkdirs();
			}
			if(!downFile.exists()) {
				downFile.createNewFile();
			}
			
		//	net.sf.json.JSONObject configJSON = net.sf.json.JSONObject.fromObject(published.getConfig());
			JSONObject configJSON = (JSONObject) JSONObject.toJSON(published.getConfig());
			String config = configJSON.toString();
		//	net.sf.json.JSONArray widgetJSON = net.sf.json.JSONArray.fromObject(published.getWidget());
			JSONArray widgetJSON = (JSONArray) JSONArray.toJSON(published.getWidget());
			String widget = widgetJSON.toString();
			
			PrintStream ps = new PrintStream(new FileOutputStream(downFile));
			ps.print("{");
			ps.append("\"config\":" + config);
			ps.append(",\"widget\":" + widget);
			ps.append("}");
			
			String result = cmdExec(Utils.getRootPath() + "/scripts/pack.sh", "public/files/"+fileName);
			return new CommonResult().success(result);
		}
		return new CommonResult().failed();
	}
	
	public CommonResult moveDashboardById(String key, String id) {
		System.out.println("key :"+key);
		Dashboard dashboard = dashboardRepository.findByHash(id);
		if(dashboard == null) {
			return new CommonResult().customFailed("查询数据库失败！");
		}
		
		if(!dashboard.getHash().equals("")) {
			dashboard.setProject(key);
			dashboardRepository.save(dashboard);
			return new CommonResult().success("分组成功!");
		}else {
		    return new CommonResult().customFailed("移动分组失败");
		}
	}

}
