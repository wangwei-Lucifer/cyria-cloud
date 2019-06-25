package com.kunteng.cyria.dashboard.domain;

import java.time.LocalDate;
import java.util.Date;

public class Config {
	private Boolean page;
	private String title;
	private String about;
	private Integer width;
	private Integer height;
	private Integer zoom;
	private String backgroundColor;
	private String backPic;
	private Date timestamp;

	public Config(){
		title = "";
		about = "";
		page = true;
		width = 1920;
		height = 1080;
		zoom = 100;
		backgroundColor = "#FFFFFF";
		backPic = "";
		timestamp = new Date();
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

	public Date getTimestamp(){
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp){
		this.timestamp = timestamp;
	}
}
