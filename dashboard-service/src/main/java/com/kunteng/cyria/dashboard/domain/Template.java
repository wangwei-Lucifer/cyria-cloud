package com.kunteng.cyria.dashboard.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import net.sf.json.JSONArray;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.time.LocalDate;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;

@Document(collection = "templates")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Template {
	@Id
	private String id;

	private String hash;

	private Config config;

	private JSONArray widget;
	//private String[] widget;
	//private HashMap<String,String> widget;

	private Integer level;

	private String imgUrl;
	
	private String imgData;

	private LocalDate timestamp;

	public Template() {
		hash = Publish.generateId();
		config = new Config();
		//widget = new ArrayList<String>();
		widget = new JSONArray();
	//	widget = new HashMap<String,String>();
		level = 0;
		imgUrl = "";
		imgData = "";
		timestamp = LocalDate.now();
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(){
		this.hash = Publish.generateId();
	}

	public Config getConfig(){
		return this.config;
	}

	public void setConfig(Config config){
		this.config = config;
	}

	/*public List<String> getWidget(){
		return this.widget;
	}
	
	public void setWidget(List<String> widget){
		this.widget = widget;
	}*/
	public JSONArray getWidget() {
		return this.widget;
	}
	
	public void setWidget(JSONArray widget) {
		this.widget = widget;
	}

	/*public HashMap<String,String> getWidget(){
		return this.widget;
	}
	
	public void setWidget(HashMap<String,String> widget) {
		this.widget = widget;
	}*/
	public Integer getLevel(){
		return this.level;
	}

	public void setLevel(Integer level){
		this.level = level;
	}

	public String getImgUrl(){
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
	}

	public LocalDate getTimestamp(){
		return this.timestamp;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}
	
	public static Boolean isNotEmpty(Template tp) {
		return !(tp == null);
	}

	public String getImgData() {
		return this.imgData;
	}

	public void setImgData(String imgData) {
		this.imgData = imgData;
	}
}
