package com.kunteng.cyria.dashboard.service;

import org.springframework.data.domain.Page;

import com.kunteng.cyria.dashboard.domain.Published;
import com.kunteng.cyria.dashboard.domain.Template;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.utils.CommonResult;

public interface TemplateService {
	CommonResult createNewTemplate(Translation translation);

	CommonResult getAllTemplates(Integer page, Integer size);

	CommonResult getTemplateById(String id);

	CommonResult deleteTemplateById(String id);

	CommonResult updateTemplateById(String id, String tp);
}
