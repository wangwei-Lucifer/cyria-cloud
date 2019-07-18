package com.kunteng.cyria.dashboard.domain;

import java.util.ArrayList;

public class WidgetMap {
	private String widgetUUID;
	private ArrayList<String> csvTitles;
	
	public WidgetMap() {
		widgetUUID = "";
		csvTitles = new ArrayList<>();
	}

	public String getWidgetUUID() {
		return this.widgetUUID;
	}
	
	public void setWidgetUUID(String widgetUUID) {
		this.widgetUUID = widgetUUID;
	}
	
	public ArrayList<String> getCsvTitles(){
		return this.csvTitles;
	}
	
	public void setCsvTitles(ArrayList<String> csvTitles) {
		this.csvTitles = csvTitles;
	}
}
