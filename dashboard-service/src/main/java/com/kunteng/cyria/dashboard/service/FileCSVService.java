package com.kunteng.cyria.dashboard.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.utils.CommonResult;

public interface FileCSVService {

	CommonResult uploadFileCSV(MultipartFile file) throws IOException;
	
}
