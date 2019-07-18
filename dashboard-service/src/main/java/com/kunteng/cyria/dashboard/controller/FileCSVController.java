package com.kunteng.cyria.dashboard.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.service.FileCSVService;
import com.kunteng.cyria.dashboard.utils.CommonResult;

@RestController
public class FileCSVController {
	
	@Autowired
	private FileCSVService fileCSVService;
	
	@RequestMapping(path = "/material/csv", method = RequestMethod.POST)
	public CommonResult uploadFileCSV(@RequestParam("file") MultipartFile file) throws IOException{
		return fileCSVService.uploadFileCSV(file);
	}

	@RequestMapping(path = "/material/list", method = RequestMethod.GET)
	public CommonResult getCSVList(@RequestParam Map<String,Object> map){
		return fileCSVService.getCSVList(map);
	}
	
	@RequestMapping(path = "/material/save", method = RequestMethod.POST)
	public CommonResult saveCSVTitle(@RequestParam Map<String,Object> map){
		return fileCSVService.saveCSVTitle(map);
	}
}
