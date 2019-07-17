package com.kunteng.cyria.dashboard.domain;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rawCSV")
public class RawCSV {
	private String fileName;
	private  ArrayList<TitleCell> title;
	private ArrayList<DataCell> data;
	
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
	
	
	public ArrayList<DataCell> getData() {
		return this.data;
	}
	
	public void setData(ArrayList<DataCell> data) {
		this.data = data;
	}
}
