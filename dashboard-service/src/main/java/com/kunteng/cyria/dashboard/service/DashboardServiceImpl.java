package com.kunteng.cyria.dashboard.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
import com.kunteng.cyria.dashboard.utils.RequestWrapper;
import com.kunteng.cyria.dashboard.utils.Utils;
import com.mongodb.util.JSON;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

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
			}
		}

		System.out.printf("user=%s, page =%d, limit =%d, title= %s, status = %s\n",user, page,limit,title, status);
		Sort sort = new Sort(Sort.Direction.ASC,"timestamp");
		PageRequest pageRequest = new PageRequest(page-1, limit, sort);
		List<Dashboard> dashboard = null;
		long sum = 0;
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

		Map<String,Object> result = new HashMap<>();
		result.put("items", dashboard);
		result.put("total", sum);
		return new CommonResult().success(result);
	}

	public CommonResult getDashboardById(String id) {
		Dashboard dashboard =  dashboardRepository.findByHash(id);
		return new CommonResult().success(dashboard);
	}
	
	public CommonResult createNewDashboard(String id, Translation translation){
		log.info("translation=" + translation.toString());

		if(!translation.getIsTemplate()) {
			Dashboard dashboard = new Dashboard();
			
			dashboard.getConfig().setTitle(translation.getName());
			dashboard.getConfig().setAbout(translation.getAbout());
			if(!translation.getTemplateId().equals("")) {
				if(templateRepository.exists(translation.getTemplateId())) {
					Template template = templateRepository.findByHash(translation.getTemplateId());
					dashboard.getConfig().setWidth(template.getConfig().getWidth());
					dashboard.getConfig().setHeigth(template.getConfig().getHeight());
					dashboard.getConfig().setZoom(template.getConfig().getZoom());
					dashboard.getConfig().setBackgroundColor(template.getConfig().getBackgroundColor());
					dashboard.getConfig().setBackPic(template.getConfig().getBackPic());
					dashboard.setWidget(template.getWidget());
				}
			}
			dashboard.getConfig().setTimestamp(new Date());
			dashboard.getPublish().setStatus("unpublished");
			dashboard.getPublish().setTimestamp(new Date());
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

		dashboard.getPublish().setStatus(opt);
		dashboard.getPublish().setTimestamp(new Date());
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
			Published publish =publishedRepository.save(published);
			result.setCode(0);
			result.setData(publish);
			result.setMsg("发布成功");
		}
		
		if(opt.equals("republish")){
			dashboard.getPublish().setStatus("published");
			Published published = publishedRepository.findByHash(dashboard.getPublish().getHash());
			published.setConfig(dashboard.getConfig());
			published.setWidget(dashboard.getWidget());
			Published publish = publishedRepository.save(published);
			result.setCode(0);
			result.setData(publish);
			result.setMsg("重新发布成功");
		}
		dashboardRepository.save(dashboard);
		return result;
	}

	public CommonResult updateDashboardById(String id, String db) throws Exception{
		JSONObject jso = JSONObject.fromObject(db);
		Dashboard dashboard = dashboardRepository.findByHash(id);
		if(jso.has("config")) {
			JSONObject configObject = jso.getJSONObject("config");
			Config config = (Config)JSONObject.toBean(configObject, Config.class);
			dashboard.setConfig(config);
		}
		if(jso.has("widget")) {
			JSONArray widget = jso.getJSONArray("widget");
			dashboard.setWidget(widget);
		}
		if(jso.has("imgData")) {
			//String path = ResourceUtils.getURL("classpath:").getPath()
			String imgUrl = Utils.createImage(jso.getString("imgData"),id);
	//		dashboard.setImgData(jso.getString("imgData"));
			dashboard.setImgUrl(imgUrl);
		}
		Dashboard result = dashboardRepository.save(dashboard);
		return new CommonResult().success(result);
	}
	
	public CommonResult getPublishedById(String id) {
		Published published = publishedRepository.findByHash(id);
		return new CommonResult().success(published);
	}

	@Override
	public CommonResult uploadImage(String id, MultipartFile files) throws IllegalStateException, IOException {
		return new CommonResult().success(Utils.uploadImage(id, files));
	}

}
