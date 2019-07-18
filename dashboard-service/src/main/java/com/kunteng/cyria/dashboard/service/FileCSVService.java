package com.kunteng.cyria.dashboard.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.utils.CommonResult;

public interface FileCSVService {

	CommonResult uploadFileCSV(MultipartFile file) throws IOException;

	CommonResult getCSVList(Map<String, Object> map);

	CommonResult saveCSVTitle(Map<String, Object> map);
	
}
