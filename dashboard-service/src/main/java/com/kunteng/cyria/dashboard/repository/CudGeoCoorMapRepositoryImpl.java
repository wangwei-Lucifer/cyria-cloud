package com.kunteng.cyria.dashboard.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.esotericsoftware.minlog.Log;
import com.kunteng.cyria.dashboard.domain.GeoCoorMap;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

@Repository
public class CudGeoCoorMapRepositoryImpl implements CudGeoCoorMapRepository {


	private final MongoTemplate mongo;
	
	@Autowired
	public CudGeoCoorMapRepositoryImpl(MongoTemplate mongo) {

		this.mongo = mongo;
	}
	
	@Override
	public String upsert(Query query, GeoCoorMap entity) {
		
		Assert.notNull(entity);

		if (query == null) {
			query = new Query(Criteria.where("province").is(entity.getProvince()).and("city").is(entity.getCity()).and("county").is(entity.getCounty()));
		}
		Update dataset = new Update();
		dataset.set("province", entity.getProvince());
		dataset.set("city", entity.getCity());
		dataset.set("county", entity.getCounty());
		dataset.set("zipCode", entity.getZipCode());
		dataset.set("location", entity.getLocation());

		WriteResult dbret = mongo.upsert(query, dataset, GeoCoorMap.class);
		
		if (dbret.getUpsertedId() == null)
			return null;
		
		return dbret.getUpsertedId().toString();
	}

	public List<GeoCoorMap> findAll() {
		return this.mongo.findAll(GeoCoorMap.class);
	}
	
	public List<GeoCoorMap> findBy(Map<String,String> cnd) {
		if ( cnd.isEmpty() )
			return this.findAll();
		
		Query query = new Query();
		Criteria c = null;
		for (Map.Entry<String, String> e : cnd.entrySet()) {
			if ( c == null )
				c = Criteria.where(e.getKey()).is(e.getValue());
			else
				c = c.and(e.getKey()).is(e.getValue());
		}
		query.addCriteria(c);
		Log.info(query.toString());
		return this.mongo.find(query, GeoCoorMap.class);
	}
	public Boolean delete(String id) {
		try {
			this.mongo.remove(new GeoCoorMap(id));
			return true;
		}
		catch (MongoException e) {
			return false;
		}
	}
}
