package com.kunteng.cyria.dashboard.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.domain.GeoCoorMap;
import com.kunteng.cyria.dashboard.utils.CommonResult;

public interface GeoCoorMapService {
	CommonResult saveGeoMap(GeoCoorMap entity);
	CommonResult batchSave(List<GeoCoorMap> items );
	CommonResult csvSave(MultipartFile file);
	CommonResult listAll();
	CommonResult listBy(Map<String, String> paras);
	CommonResult delete(String id);
}
