package com.kunteng.cyria.dashboard.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection = "GeoCoorMapOfChina")
public class GeoCoorMap {
	@Id
	private String id;
	private String province;
	private String city;
	private String county;
	private String zipCode;
	private GeoJsonPoint location;
	
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
}
