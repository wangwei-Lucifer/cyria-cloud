package com.kunteng.cyria.dashboard.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64.Decoder;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kunteng.cyria.dashboard.domain.GeoCoorMap;
import com.kunteng.cyria.dashboard.service.GeoCoorMapService;
import com.kunteng.cyria.dashboard.utils.CommonResult;

@RestController
public class GeoCoorMapController {

	@Autowired
	private GeoCoorMapService service;
	
	/**
	 * 通过 post 方法提交一条城市坐标数据。curl 请求格式如下：
	 * curl --data-raw '{ "province":"山东", "city":"烟台", "county":"文登", "location": { "type":"Point","coordinates":[122.05,37.2]}, "zipcode":null }'
	 *  -H "Content-Type:application/json" http://localhost:4000/system/geodata/saveOne
	 * 提交数据采用json格式，例如：
	 * { “province”:"内蒙古", "city":"鄂尔多斯", "county":"", 
	 * 	 "location": { "type":"Point", "coordinates": [ 109.781, 39.608 ] },
	 *   "zipcode":null }  
	 * @return
	 */
	@RequestMapping(path="/system/geodata/saveOne", method=RequestMethod.POST)
	public CommonResult batchSave(@RequestBody GeoCoorMap entity) {
		return this.service.saveGeoMap(entity);
	}
	/**
	 * 通过 post 方法提交一条或多条城市坐标数据。curl REST请求格式如下：
	 * curl -data-raw <data> -H "Content-Type:application/json;charset:UTF-8" http://<server>:<port>/system/geodata/save
	 * 提交数据采用json格式，例如：
	 * [{ “province”:"内蒙古", "city":"鄂尔多斯", "county":"", 
	 * 	 "location": { "type":"Point", "coordinates": [ 109.781, 39.608 ] },
	 *   "zipcode":null }
	 *   ...
	 * ]  
	 * @return
	 */
	@RequestMapping(path="/system/geodata/save", method=RequestMethod.POST)
	public CommonResult batchSave(@RequestBody List<GeoCoorMap> items) {
		return this.service.batchSave(items);
	}
	
	/**
	 * 通过上传一个csv文件方式提交数据。curl 命令格式如下：
	 * curl -F "file=@geoCoorMap.csv" http://<server>:4000/system/geodata/saveByFile
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(path="/system/geodata/saveByFile", method=RequestMethod.POST)
	public CommonResult batchSaveByFile(@RequestParam("file") MultipartFile file) throws IOException {
/*		Log.info(file.getOriginalFilename());
		Log.info(file.getName());
		Log.info(file.getContentType());
		Log.info(file.getInputStream().toString());
*/		
		return this.service.csvSave(file);
	}
	/**
	 * 获取指定城市及其下属城市的坐标信息。curl 请求格式如下：
	 * curl http://<server>:<port>/system/geodata/list?province='XXX'&city='xxx'&county='xxx'
	 * @param paras
	 * 参数可以通过province指定匹配的省名，或通过city指定匹配的地级市名 、以及county指定的县市或区名，可以任意组合
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JsonProcessingException 
	 */
	@RequestMapping(path="/system/geodata/list", method=RequestMethod.GET)
//	public CommonResult getGeoMAp(@RequestParam(value="province", required=false) String province,
//			@RequestParam(value="city", required=false) String city,
//			@RequestParam(value="county",required=false) String county
//	public CommonResult getGeoMAp(HttpServletRequest request
	public CommonResult getGeoMap(@RequestParam Map<String,String> paras
	) throws UnsupportedEncodingException, JsonProcessingException {
//		Log.info(request.getQueryString());
//		java.util.Enumeration<String> nm = request.getHeaderNames();
//		while (nm.hasMoreElements()) {
//			String str = (String) nm.nextElement();
//			Log.info(String.format("%s :  %s", str, request.getHeader(str)));
//		}
//		Log.info(request.getContentType());
//		Log.info(request.getCharacterEncoding());

		//Log.info(String.format("Total params: %s, %s, %s", province,city,county));
		
//		String province = request.getParameter("province");
//		String city = request.getParameter("city");
//		String county = request.getParameter("county");
//		Map<String,String> paras = request.getParameterMap();
//		if (province != null && !province.isEmpty()) {
//			province = new String(province.getBytes("ISO-8859-1"), "UTF-8");
//			paras.put("province", province);
//		}
//		if (city != null && !city.isEmpty()) {
//			city = new String(city.getBytes("ISO-8859-1"), "UTF-8");
//			paras.put("city", city);
//		}
//		if (county != null && !county.isEmpty()) {
//			county = new String(county.getBytes("ISO-8859-1"), "UTF-8");
//			paras.put("county", county);
//		}
		
		Log.info(paras.toString());
		return this.service.listBy(paras);
	}
	/**
	 * 获取指定省的下属城市坐标信息。请求格式如下：
	 * http://<server>:<port>/system/geodata/getCityLocationsBy?province='XXX'
	 * @param paras
	 * 参数可以通过province指定匹配的省名，不指定则获取所有省的直接下属城市或地区的坐标。
	 * 
	 * @return '城市名':[x,y] 格式的字符串列表。
	 * @throws UnsupportedEncodingException 
	 * @throws JsonProcessingException 
	 */
	@RequestMapping(path="/system/geodata/getCityLocationsBy", method=RequestMethod.GET)
	public CommonResult getCityLocationsBy(@RequestParam Map<String,String> paras) throws UnsupportedEncodingException, JsonProcessingException {
		paras.remove("city");
		paras.put("county", null);
		CommonResult ret = new CommonResult();
		List<GeoCoorMap> entities = this.service.getBy(paras);
		Map<String,double[]> data = new HashMap<String,double[]>();
		for (GeoCoorMap entity : entities) {
			Map<String,double[]> c = entity.cityLocToJson();
			if (c != null)
				data.putAll(c);
		}
		return ret.success(data);				
	}
	/**
	 * 获取指定地级市的下属城市坐标信息。请求格式如下：
	 * http://<server>:<port>/system/geodata/getCountyLocationsBy?province='XXX'&city='xxx'
	 * @param paras
	 * 参数可以通过province指定匹配的省名，不指定则获取所有省的直接下属城市或地区的坐标。
	 * 
	 * @return '城市名':[x,y] 格式的字符串列表。
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(path="/system/geodata/getCountyLocationsBy", method=RequestMethod.GET)
	public CommonResult getCountyLocationsBy(@RequestParam Map<String,String> paras) throws UnsupportedEncodingException {
		paras.remove("county");
		CommonResult ret = new CommonResult();
		List<GeoCoorMap> entities = this.service.getBy(paras);
		Map<String,double[]> data = new HashMap<String,double[]>();
		for (GeoCoorMap entity : entities) {
			Map<String,double[]> c  = entity.countyLocToJson();
			if (c != null)
				data.putAll(c);
		}
		return ret.success(data);				
	}
	/**
	 * 删除指定ID的文档数据。curl 请求形式如下：
	 * curl -X DELETE http://<server>:<port>/system/geodata/{id}/delete
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/system/geodata/{id}/delete", method=RequestMethod.DELETE)
	public CommonResult deleteGeoMap(@PathVariable String id) {
		return this.service.delete(id);
	}
}
