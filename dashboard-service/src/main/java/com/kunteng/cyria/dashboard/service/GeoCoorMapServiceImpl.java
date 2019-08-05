package com.kunteng.cyria.dashboard.service;

import java.io.IOException;
import java.io.InputStreamReader;
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
import com.kunteng.cyria.dashboard.domain.GeoCoorMap;
import com.kunteng.cyria.dashboard.repository.*;
import com.kunteng.cyria.dashboard.utils.CommonResult;

@Service
public class GeoCoorMapServiceImpl implements GeoCoorMapService{

	@Autowired
	private CudGeoCoorMapRepository repo;
	
	public CommonResult saveGeoMap(GeoCoorMap entity) {
		CommonResult ret = new CommonResult();
		String rid = this.repo.upsert(null, entity);
		if ( rid != null) {
			entity.setId(rid);
			return ret.success(entity);
		}
		else
			return ret.failed();
	}
	
	public CommonResult batchSave(List<GeoCoorMap> items) {
		CommonResult ret = new CommonResult();
		
		if ( items == null)
			return ret.success(items);
		
		for (GeoCoorMap data:items) {
			String rid = this.repo.upsert(null, data);
			data.setId(rid);
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
			for(String field : line) {
				Log.info(field);
			}
			// 
			line=reader.readNext();
			while( line != null ) {
				for ( String field : line) {
					Log.info(field);
				}
				
				line = reader.readNext();
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ret.customFailed(e.getMessage());
		}
		return ret.success(null);
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
	public CommonResult delete(String id) {
		CommonResult ret = new CommonResult();
		if (this.repo.delete(id) )
			return ret.success(null);
		return ret.failed();
	}
}
