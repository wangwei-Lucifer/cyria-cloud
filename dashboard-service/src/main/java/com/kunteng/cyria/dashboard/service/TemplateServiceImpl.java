package com.kunteng.cyria.dashboard.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kunteng.cyria.dashboard.domain.Config;
import com.kunteng.cyria.dashboard.domain.Template;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.repository.TemplateRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TemplateServiceImpl implements TemplateService {
	
	@Autowired
	private TemplateRepository templateRepository;
	
	@Override
	public Template createNewTemplate(Translation translation){
		if(translation.getIsTemplate()) {
			Template template = new Template();
			template.getConfig().setTitle(translation.getName());
			template.getConfig().setAbout(translation.getAbout());
			template.setTimestamp(LocalDate.now());
			templateRepository.save(template);
			return template;
			
		}else {
			return new Template();
		}
	}

	@Override
	public Page<Template> getAllTemplates(Integer page, Integer size) {
		Sort sort = new Sort(Sort.Direction.ASC,"timestamp");
		PageRequest pageRequest = new PageRequest(page, size, sort);
		Page<Template> template = templateRepository.findAll(pageRequest);
		return template;
	}

	@Override
	public Template getTemplateById(String id) {
		Template template = templateRepository.findByHash(id);
		return template;
	}
	
	@Override
	public Template updateTemplateById(String id, String tp) {
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
			template.setImgData(jso.getString("imgData"));
		}
		
		templateRepository.save(template);
		return template;
	}
	
	@Override
	public void deleteTemplateById(String id) {
		templateRepository.deleteByHash(id);
	}
}
