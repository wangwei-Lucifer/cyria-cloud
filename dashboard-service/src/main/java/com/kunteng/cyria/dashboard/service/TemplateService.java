package com.kunteng.cyria.dashboard.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.kunteng.cyria.dashboard.domain.Published;
import com.kunteng.cyria.dashboard.domain.Template;
import com.kunteng.cyria.dashboard.domain.Translation;
import com.kunteng.cyria.dashboard.utils.CommonResult;

public interface TemplateService {
	CommonResult createNewTemplate(Translation translation);

	CommonResult getAllTemplates(Map<String,Object> map);

	CommonResult getTemplateById(String id);

	CommonResult deleteTemplateById(String id);

	CommonResult updateTemplateById(String id, String tp);
}
