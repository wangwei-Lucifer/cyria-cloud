package com.kunteng.cyria.dashboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kunteng.cyria.dashboard.domain.Dashboard;
import com.kunteng.cyria.dashboard.domain.Published;
import com.kunteng.cyria.dashboard.domain.Template;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.service.TemplateService;
import com.kunteng.cyria.dashboard.utils.CommonResult;

@RestController
public class TemplateController {
	
	@Autowired
	private TemplateService templateService;
	
	@RequestMapping(path = "/templates", method = RequestMethod.GET)
	public CommonResult getAllTemplates(@RequestParam Map<String,Object> map){
		return templateService.getAllTemplates(map);
	}
	
	@RequestMapping(path = "/templates/{id}", method = RequestMethod.GET)
	public CommonResult getTemplateById(@PathVariable String id) {
		return templateService.getTemplateById(id);
	}
	
	@RequestMapping(path = "/templates", method = RequestMethod.POST)
	public CommonResult  createNewTemplate(@RequestBody Translation translation) {
		return templateService.createNewTemplate(translation);
	}
	
	@RequestMapping(path ="/templates/{id}", method = RequestMethod.PUT)
	public CommonResult updateTemplateById(@PathVariable String id, @RequestBody String template) {
		return templateService.updateTemplateById(id, template);
	}
	
	@RequestMapping(path = "/templates/{id}", method = RequestMethod.DELETE)
	public CommonResult deleteTemplateById(@PathVariable String id) {
		return templateService.deleteTemplateById(id);
	}
}
