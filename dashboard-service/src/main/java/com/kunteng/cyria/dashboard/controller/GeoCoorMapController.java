package com.kunteng.cyria.dashboard.controller;

import java.util.List;
import java.util.Map;
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
	 *  -H "Content-Type:application/json" http://localhost:4000/system/geodata/save
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
	 * curl -F "file=@geoCoorMap.csv" http://<server>:4000/system/geodata/saveWithFile
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(path="/system/geodata/saveWithFile", method=RequestMethod.POST)
	public CommonResult batchSaveWithFile(@RequestParam("file") MultipartFile file) throws IOException {
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
	 */
	@RequestMapping(path="/system/geodata/list", method=RequestMethod.GET)
	public CommonResult getGeoMAp(@RequestParam Map<String,String> paras) throws UnsupportedEncodingException {
		if ( paras.isEmpty() )
			return this.service.listAll();

		for ( Map.Entry<String, String> entry : paras.entrySet()) {
			String s = new String(entry.getValue().getBytes("ISO-8859-1"), "UTF-8");
			entry.setValue(s);
		}
		Log.info(paras.toString());
		return this.service.listBy(paras);
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
