package com.kunteng.cyria.dashboard.controller;

import java.io.IOException;
import java.util.Map;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kunteng.cyria.dashboard.domain.RawCSV;
import com.kunteng.cyria.dashboard.service.FileCSVService;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import com.kunteng.cyria.dashboard.repository.FinalCSVRepository;
import com.kunteng.cyria.dashboard.domain.FinalCSV;
import com.kunteng.cyria.dashboard.domain.TitleCell;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Slf4j
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
	
	@RequestMapping(path = "/material/{hash}/save", method = RequestMethod.POST)
	public CommonResult saveCSVTitle(@PathVariable String hash, @RequestBody RawCSV rawCSV){
		return fileCSVService.saveCSVTitle(hash, rawCSV);
	}
	
	@RequestMapping(path = "/material/{hash}/view", method = RequestMethod.GET)
	public CommonResult viewCSVByHash(@PathVariable String hash) {
		return fileCSVService.viewCSVByHash(hash);
	}
	
	@RequestMapping(path = "/material/{hash}/delete", method = RequestMethod.DELETE)
	public CommonResult deleteCSVByHash(@PathVariable String hash) {
		return fileCSVService.deleteCSVByHash(hash);
	}
	
	@RequestMapping(path = "/material/{hash}/update", method = RequestMethod.POST)
	public CommonResult updateCSVData(@PathVariable String hash, @RequestParam MultipartFile file) throws IOException{
		return fileCSVService.updateCSVData(hash, file);
	}
	
	@RequestMapping(path = "/material/titleList", method = RequestMethod.GET)
	public CommonResult getTitleList() {
		return fileCSVService.getTitleList();
	}
	
	@RequestMapping(path = "/material/{hash}/cancel", method = RequestMethod.GET)
	public CommonResult cancelCSV(@PathVariable String hash) {
		return fileCSVService.cancelCSV(hash);
	}

	@RequestMapping(value = "/material/jsonapi", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String jsonapi(@RequestParam(defaultValue = "") String groups, @RequestParam(defaultValue = "") String values,
			@RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") String suuid,
			@RequestParam(defaultValue = "") String source) {
                return fileCSVService.getJsonApiData(groups,values,type,suuid,source);
        }
}
