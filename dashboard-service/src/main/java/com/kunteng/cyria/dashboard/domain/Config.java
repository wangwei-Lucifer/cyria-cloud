package com.kunteng.cyria.dashboard.domain;

import java.time.LocalDate;

public class Config {
	private String title;
	private String about;
	private Integer width;
	private Integer height;
	private Integer zoom;
	private String backgroupColor;
	private String backPic;
	private LocalDate timestamp;

	public Config(){
		title = "";
		about = "";
		width = 1920;
		height = 1080;
		zoom = 100;
		backgroupColor = "#FFFFFF";
		backPic = "";
		timestamp = LocalDate.now();
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

	public void setHeigth(Integer height){
		this.height = height;
	}

	public Integer getZoom(){
		return this.zoom;
	}

	public void setZoom(Integer zoom){
		this.zoom = zoom;
	}

	public String getBackgroupColor(){
		return this.backgroupColor;
	}

	public void setBackgroupColor(String backgroupColor){
		this.backgroupColor = backgroupColor;
	}

	public String getBackPic(){
		return this.backPic;
	}

	public void setBackPic(String backPic){
		this.backPic = backPic;
	}

	public LocalDate getTimestamp(){
		return this.timestamp;
	}

	public void setTimestamp(LocalDate timestamp){
		this.timestamp = timestamp;
	}
}
