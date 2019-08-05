package com.kunteng.cyria.dashboard.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Config {
	private Boolean page;
	private String title;
	private String about;
	private Integer width;
	private Integer height;
	private Integer zoom;
	private String backgroundColor;
	private String backPic;
	private long timestamp;
	private Map<String,Object> colors;

	public Config(){
		title = "";
		about = "";
		page = true;
		width = 1920;
		height = 1080;
		zoom = 100;
		backgroundColor = "#000000";
		backPic = "";
		timestamp = new Date().getTime();
		colors = new HashMap<String,Object>() {{put("name","配色1");put("value",new String[] {"#8378ea", "#96bfff", "#37a2da", "#32c5e9", "#67e0e3", "#9fe6b8", "#ffdb5c", "#ff9f7f", "#fb7293", "#e062ae", "#e690d1", "#e7bcf3", "#9d96f4"});}};
	}
	
	public Boolean getPage() {
		return this.page;
	}
	
	public void setPage(Boolean page) {
		this.page = page;
	}

	public String getTitle(){
		return this.title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getAbout(){
		return this.about;
	}

	public void setAbout(String about){
		this.about = about;
	}

	public Integer getWidth(){
		return this.width;
	}

	public void setWidth(Integer width){
		this.width = width;
	}

	public Integer getHeight(){
		return this.height;
	}

	public void setHeight(Integer height){
		this.height = height;
	}

	public Integer getZoom(){
		return this.zoom;
	}

	public void setZoom(Integer zoom){
		this.zoom = zoom;
	}

	public String getBackgroundColor(){
		return this.backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor){
		this.backgroundColor = backgroundColor;
	}

	public String getBackPic(){
		return this.backPic;
	}

	public void setBackPic(String backPic){
		this.backPic = backPic;
	}

	public long getTimestamp(){
		return this.timestamp;
	}

	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}

	public Map<String,Object> getColors() {
		return this.colors;
	}

	public void setColors(Map<String,Object> colors) {
		this.colors = colors;
	}
}
