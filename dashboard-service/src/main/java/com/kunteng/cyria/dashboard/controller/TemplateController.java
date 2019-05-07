package com.kunteng.cyria.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kunteng.cyria.dashboard.domain.Dashboard;
import com.kunteng.cyria.dashboard.domain.Published;
import com.kunteng.cyria.dashboard.domain.Template;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.service.TemplateService;

@RestController
public class TemplateController {
	
	@Autowired
	private TemplateService templateService;
	
	@RequestMapping(path = "/templates", method = RequestMethod.GET)
	public Page<Template> getAllTemplates(int page, int size){
		return templateService.getAllTemplates(page,size);
	}
	
	@RequestMapping(path = "/templates/{id}", method = RequestMethod.GET)
	public Template getTemplateById(@PathVariable String id) {
		return templateService.getTemplateById(id);
	}
	
	@RequestMapping(path = "/templates", method = RequestMethod.POST)
	public Template  createNewTemplate(@RequestBody Translation translation) {
		return templateService.createNewTemplate(translation);
	}
	
	@RequestMapping(path ="/templates/{id}", method = RequestMethod.PUT)
	public Template updateTemplateById(@PathVariable String id, @RequestBody String template) {
		return templateService.updateTemplateById(id, template);
	}
	
	@RequestMapping(path = "/templates/{id}", method = RequestMethod.DELETE)
	public void deleteTemplateById(@PathVariable String id) {
		templateService.deleteTemplateById(id);
	}
}
