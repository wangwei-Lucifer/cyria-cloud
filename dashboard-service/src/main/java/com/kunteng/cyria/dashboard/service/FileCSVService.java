package com.kunteng.cyria.dashboard.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.domain.RawCSV;
import com.kunteng.cyria.dashboard.utils.CommonResult;

public interface FileCSVService {

	CommonResult uploadFileCSV(MultipartFile file) throws IOException;

	CommonResult getCSVList(Map<String, Object> map);

	CommonResult viewCSVByHash(String hash);
	
	CommonResult deleteCSVByHash(String hash);
	
	CommonResult saveCSVTitle(String hash, RawCSV rawCSV);
	
	CommonResult updateCSVData(String hash, MultipartFile file) throws IOException;
}
