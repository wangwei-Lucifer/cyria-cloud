package com.kunteng.cyria.dashboard.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.InputStreamReader;
import com.opencsv.CSVReader;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import com.mongodb.WriteResult;
import com.opencsv.CSVReader;
import com.esotericsoftware.minlog.Log;
import com.kunteng.cyria.dashboard.domain.*;
import com.kunteng.cyria.dashboard.repository.*;
import com.kunteng.cyria.dashboard.utils.CommonResult;

@Service
public class GeoCoorMapServiceImpl implements GeoCoorMapService{

	@Autowired
	private CudGeoCoorMapRepository repo;
	
	private String matchFieldName(String name) {
		name = name.toLowerCase();
		name = name.replaceAll("\\s*", "");
		switch( name ) {
		case "省":
		case "province":
			return "province";
		case "市":
		case "city":
			return "city";
		case "县":
		case "区":
		case "县/区":
		case "区/县":
		case "县区":
		case "区县":
		case "county":
			return "county";
		case "经度":
		case "longitude":
			return "lng";
		case "纬度":
		case "latitude":
			return "lat";
		case "zipcode":
		case "邮政编码":
		case "邮编":
			return "zipcode";
		}
		return name;
	}
	private String pregressCityAndCountyName(String name) {
		if (name ==null || name.isEmpty())
			return null;
		name = name.replaceAll("\\s*", "");
		name = name.replaceAll("市$", "");
		return name;
	}
	
	public CommonResult saveGeoMap(GeoCoorMap entity) {
		CommonResult ret = new CommonResult();
		GeoCoorMap newData = this.repo.upsert(null, entity);
		entity.cloneBy(newData);
		return ret.success(entity);
	}
	
	public CommonResult batchSave(List<GeoCoorMap> items) {
		CommonResult ret = new CommonResult();
		
		if ( items == null)
			return ret.success(items);
		
		for (GeoCoorMap data:items) {
			GeoCoorMap newData = this.repo.upsert(null, data);
			data.cloneBy(newData);
		}
		return ret.success(items);
	}

	public CommonResult csvSave(MultipartFile file) {
		CommonResult ret = new CommonResult();
		CSVReader reader;
		try {
			reader = new CSVReader(new InputStreamReader(file.getInputStream()));
			String[] line = reader.readNext();
			// 检测头部结构
			List<String> fieldsIndex = new ArrayList<String>();
			for(String field : line) {
				fieldsIndex.add(this.matchFieldName(field));
			}
			// 
			List<GeoCoorMap> geoList = new ArrayList<GeoCoorMap>();

			line=reader.readNext();
			while( line != null ) {
				GeoCoorMap item = new GeoCoorMap();
				int i=0;
				double[] point = new double[2];
				try {
					for (String field : line) {
						switch (fieldsIndex.get(i)) {
						case "province":
							Log.info(String.format("write in entity : %s", ProvinceNameOfChina.volidName(field)));
							item.setProvince(ProvinceNameOfChina.volidName(field));
							break;
						case "city":
							item.setCity(this.pregressCityAndCountyName(field));
							break;
						case "county":
							item.setCounty(this.pregressCityAndCountyName(field));
							break;
						case "lng":
							point[0] = Double.parseDouble(field.replaceAll("\\s*", ""));
							break;
						case "lat":
							point[1] = Double.parseDouble(field.replaceAll("\\s*", ""));
							break;
						case "zipcode":
							item.setZipCode(field);
							break;
						}
						i++;
					}
					GeoJsonPoint loct = new GeoJsonPoint(point[0], point[1]);
					item.setLocation(loct);
				}
				catch (NumberFormatException e) {
					item.setDbResult(e.getMessage());
				}
				geoList.add(item);
				line = reader.readNext();
			}
			reader.close();
			
			for (GeoCoorMap data:geoList) {
				GeoCoorMap ndata = this.repo.upsert(null, data);
				data.cloneBy(ndata);
			}
			return ret.success(geoList);
		} catch (IOException e) {
			e.printStackTrace();
			return ret.customFailed(e.getMessage());
		}
	}

	public CommonResult listAll() {
		CommonResult ret = new CommonResult();
		Object data = this.repo.findAll();
		return ret.success(data);		
	}
	public CommonResult listBy(Map<String, String> paras) {
		CommonResult ret = new CommonResult();
		Object data = this.repo.findBy(paras);
		return ret.success(data);		
	}
	public List<GeoCoorMap> getBy(Map<String, String> paras) {
		return this.repo.findBy(paras);
	}
	public CommonResult delete(String id) {
		CommonResult ret = new CommonResult();
		if (this.repo.delete(id) )
			return ret.success(null);
		return ret.failed();
	}
}
