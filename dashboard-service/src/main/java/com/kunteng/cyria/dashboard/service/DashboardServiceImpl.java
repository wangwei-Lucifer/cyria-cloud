package com.kunteng.cyria.dashboard.service;

import java.io.IOException;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

	public CommonResult getAllDashboard(String user, Integer page, Integer size) {
		Sort sort = new Sort(Sort.Direction.ASC,"timestamp");
		PageRequest pageRequest = new PageRequest(page-1, size, sort);
		Page<Dashboard> dashboard = dashboardRepository.findByUser(user, pageRequest);
		
		return new CommonResult().success(dashboard);
	}

	public CommonResult getDashboardById(String id) {
		Dashboard dashboard =  dashboardRepository.findByHash(id);
		return new CommonResult().success(dashboard);
	}
	
	public CommonResult createNewDashboard(String id, Translation translation){
		log.debug("translation=" + translation);

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
					dashboard.getConfig().setBackgroupColor(template.getConfig().getBackgroupColor());
					dashboard.getConfig().setBackPic(template.getConfig().getBackPic());
					dashboard.setWidget(template.getWidget());
				}
			}
			dashboard.getConfig().setTimestamp(LocalDate.now());
			dashboard.getPublish().setStatus("unpublished");
			dashboard.getPublish().setTimestamp(LocalDate.now());
			dashboard.setUser(translation.getUser());

			dashboardRepository.save(dashboard);
			return new CommonResult().success(dashboard);
		}
		return new CommonResult().failed();
	}
	
	public CommonResult deleteDashboardByHash(String id) {
		String result =  dashboardRepository.deleteByHash(id);
		return new CommonResult().success(result);
	}

	public CommonResult publishDashboardById(String id, String option){
		System.out.println("option="+option);
		System.out.println("id="+id);
		CommonResult result = new CommonResult();
		option=option.trim();
		Dashboard dashboard = dashboardRepository.findByHash(id);

		dashboard.getPublish().setStatus(option);
		dashboard.getPublish().setTimestamp(LocalDate.now());
		System.out.println("hash1="+ dashboard.getPublish().getHash());
		if(option.equals("unpublished=")){
			publishedRepository.deleteByHash(dashboard.getPublish().getHash());
			System.out.println("hash2="+ dashboard.getPublish().getHash());
			dashboard.getPublish().setHash("");
			result.setCode(0);
			result.setMessage("停止发布成功");
			result.setData(null);
		}
		
		if(option.equals("published=")){
			System.out.println("XXXXXXXXXX");
			Published published = new Published();
			dashboard.getPublish().setHash(published.getHash());
			published.setConfig(dashboard.getConfig());
			published.setWidget(dashboard.getWidget());
			Published publish =publishedRepository.save(published);
			result.setCode(0);
			result.setData(publish);
			result.setMessage("发布成功");
		}
		
		if(option.equals("republished=")){
			dashboard.getPublish().setStatus("published");
			Published published = publishedRepository.findByHash(dashboard.getPublish().getHash());
			published.setConfig(dashboard.getConfig());
			published.setWidget(dashboard.getWidget());
			Published publish = publishedRepository.save(published);
			result.setCode(0);
			result.setData(publish);
			result.setMessage("重新发布成功");
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
