package com.kunteng.cyria.dashboard.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Document( collection = "GeoCoorMapOfChina")
public class GeoCoorMap {
	@Id
	private String id;
	private String province;
	private String city;
	private String county;
	private String zipCode;
	private GeoJsonPoint location;
	@Transient
	private String dbResult;
	
	private static String generateId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }

	public GeoCoorMap( ) {
		this.id = generateId();
	}
	public GeoCoorMap(String id) {
		this.id = id;
	}
	
	public void cloneBy(GeoCoorMap clone) {
		if (clone == null)
			return;
		
		this.id = clone.id;
		this.province = clone.province;
		this.city = clone.city;
		this.county = clone.county;
		this.zipCode = clone.zipCode;
		this.location = clone.location;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String s) {
		this.id = s;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public GeoJsonPoint getLocation() {
		return location;
	}
	public void setLocation(GeoJsonPoint coordinate) {
		this.location = coordinate;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getDbResult( ) {
		return dbResult;
	}
	public void setDbResult(String msg) {
		dbResult = msg;
	}
	
	public Map<String,double[]> cityLocToJson( ) {
		if (this.city==null || this.city.isEmpty())
			return null;

		try {
			double[] c = { this.location.getX(),this.location.getY() };
			Map<String,double[]> m = new HashMap<String,double[]>();
			m.put(this.city, c);
			return m;
		}
		catch(Exception e) {
			Log.info(e.getMessage());
		}
		return null;
	}
	public Map<String,double[]> countyLocToJson()  {
		if (this.county==null || this.county.isEmpty())
			return null;
		try {
			double[] c = { this.location.getX(),this.location.getY() };
			Map<String,double[]> m = new HashMap<String,double[]>();
			m.put(this.county, c);
			return m;
		}
		catch(Exception e) {
			Log.info(e.getMessage());
		}
		return null;
	}
}
