package com.kunteng.cyria.dashboard.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kunteng.cyria.dashboard.domain.Config;
import com.kunteng.cyria.dashboard.domain.Template;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.repository.TemplateRepository;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.kunteng.cyria.dashboard.utils.Utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TemplateServiceImpl implements TemplateService {
	
	@Autowired
	private TemplateRepository templateRepository;
	
	@Override
	public CommonResult createNewTemplate(Translation translation){
		if(translation.getIsTemplate()) {
			Template template = new Template();
			template.getConfig().setTitle(translation.getName());
			template.getConfig().setAbout(translation.getAbout());
			template.getConfig().setColors(translation.getColors());
			template.setTimestamp(new Date().getTime());
			templateRepository.save(template);
			String hash = template.getHash();
			return new CommonResult().customHash(hash);
			
		}else {
			return new CommonResult().failed();
		}
	}

	@Override
	public CommonResult getAllTemplates(Map<String,Object> map) {
		int size = map.size();
		int offset = 0;
		int limit = 20;

		if(size != 0) {
			for(String key: map.keySet()) {
				if (key.equalsIgnoreCase("offset")) {
					offset = Integer.decode(map.get(key).toString());
				}
				
				if(key.equalsIgnoreCase("limit")) {
					limit = Integer.decode(map.get(key).toString());
				}
			}
		}
		System.out.printf("offset: %d, limit: %d\n",offset,limit);
		
		Sort sort = new Sort(Sort.Direction.DESC,"timestamp");
		PageRequest pageRequest = new PageRequest(offset , limit, sort);
		Page<Template> template = templateRepository.findByHashNotNull(pageRequest);
		List<Template> obj = template.getContent();
		long sum = templateRepository.count();
		Map<String,Object> result = new HashMap<>();
		result.put("items", obj);
		result.put("total",sum);
		return new CommonResult().success(result);
	}

	@Override
	public CommonResult getTemplateById(String id) {
		Template template = templateRepository.findByHash(id);
		return new CommonResult().success(template);
	}
	
	@Override
	public CommonResult updateTemplateById(String id, String tp) throws Exception  {
		JSONObject jso = JSONObject.fromObject(tp);
		Template template = templateRepository.findByHash(id);
		if(jso.has("config")) {
			JSONObject configObject = jso.getJSONObject("config");
			Config config = (Config)JSONObject.toBean(configObject, Config.class);
			template.setConfig(config);
		}
		if(jso.has("widget")) {
			JSONArray widget = jso.getJSONArray("widget");
			template.setWidget(widget);
		}
		if(jso.has("imgData")) {
		//	template.setImgData(jso.getString("imgData"));
			String imgUrl = Utils.createImage(jso.getString("imgData"),id);
			template.setImgUrl(imgUrl);
		}
		
		templateRepository.save(template);
		return new CommonResult().success(template);
	}
	
	@Override
	public CommonResult deleteTemplateById(String id) {
		 templateRepository.deleteByHash(id);
		 return new CommonResult().success("删除成功");
	}
}
