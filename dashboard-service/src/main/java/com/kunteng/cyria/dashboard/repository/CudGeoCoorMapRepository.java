package com.kunteng.cyria.dashboard.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import com.kunteng.cyria.dashboard.domain.GeoCoorMap;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.mongodb.WriteResult;


public interface CudGeoCoorMapRepository {
	List<GeoCoorMap> findAll();
	List<GeoCoorMap> findBy(Map<String, String> cnd);
	String upsert(Query query, GeoCoorMap entity);
	Boolean delete(String id);
}
