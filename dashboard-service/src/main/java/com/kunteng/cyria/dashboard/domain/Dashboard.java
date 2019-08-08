package com.kunteng.cyria.dashboard.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import net.sf.json.JSONArray;
import com.alibaba.fastjson.JSONArray;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Document(collection = "dashboards")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dashboard {
	@Id
	private String id;

	private String hash;

	private Config config;

	private JSONArray widget;

	private Publish publish;

	private Boolean isTemplate;

	private Integer level;

	private String imgUrl;
	
	private String imgData;

	private String user;
	
	private String group;

	private long timestamp;
	
	private String project;
	

	public Dashboard() {
		hash = Publish.generateId();
		config = new Config();
		widget = new JSONArray();
		publish = new Publish();
		isTemplate = false;
		level = 0;
		imgUrl = "";
		imgData = "";
		user = "";
		project = "ungrouped";
		timestamp = new Date().getTime();
	}

	public String getHash() {
		return this.hash;
	}


	public Config getConfig(){
		return this.config;
	}

	public void setConfig(Config config){
		this.config = config;
	}
	
	public JSONArray getWidget() {
		return this.widget;
	}
	
	public void setWidget(JSONArray widget) {
		this.widget = widget;
	}
               
	public Publish getPublish(){
		return this.publish;
	}

	public void setPublish(Publish publish){
		this.publish = publish;
	}

	public Boolean getIsTemplate(){
		return this.isTemplate;
	}

	public void setIsTemplate(Boolean isTemplate){
		this.isTemplate = isTemplate;
	}

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

	public String getUser(){
		return this.user;
	}

	public void setUser(String user){
		this.user = user;
	}

	public long getTimestamp(){
		return this.timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getImgData() {
		return this.imgData;
	}

	public void setImgData(String imgData) {
		this.imgData = imgData;
	}

	public String getGroup() {
		return this.group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}
}


