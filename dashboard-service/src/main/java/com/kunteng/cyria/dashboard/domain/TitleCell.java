package com.kunteng.cyria.dashboard.domain;

public class TitleCell {
	String title;
	String titleType;
	
	public TitleCell() {
		title = "";
		titleType = "text";
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitleType() {
		return this.titleType;
	}
	
	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}
}
