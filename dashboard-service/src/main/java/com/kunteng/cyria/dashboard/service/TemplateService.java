package com.kunteng.cyria.dashboard.service;

import org.springframework.data.domain.Page;

import com.kunteng.cyria.dashboard.domain.Published;
import com.kunteng.cyria.dashboard.domain.Template;
import com.kunteng.cyria.dashboard.domain.Translation;

public interface TemplateService {
	Template createNewTemplate(Translation translation);

	Page<Template> getAllTemplates(Integer page, Integer size);

	Template getTemplateById(String id);

	void deleteTemplateById(String id);

	Template updateTemplateById(String id, String tp);
}
