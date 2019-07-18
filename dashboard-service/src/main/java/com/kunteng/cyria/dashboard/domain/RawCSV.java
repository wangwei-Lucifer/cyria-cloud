package com.kunteng.cyria.dashboard.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rawCSV")
public class RawCSV {
	private String fileName; 
	private  ArrayList<TitleCell> title;
	private ArrayList<Map<String,String>> data;
	private long timestamp;
	
	public RawCSV() {
		fileName = "";
		title = new ArrayList<>();
		data = new ArrayList<>();
		timestamp = new Date().getTime();
	}
	
	public String getFileName() {
		return this.fileName;
	}                                                                                                                                                                                                           
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public ArrayList<TitleCell> getTitle(){
		return this.title;
	}
	
	public void setTitle(ArrayList<TitleCell> title) {
		this.title = title;
	}
	
	
	public ArrayList<Map<String,String>> getData() {
		return this.data;
	}
	
	public void setData(ArrayList<Map<String,String>> data) {
		this.data = data;
	}
	
	public long getTimestamp(){
		return this.timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
