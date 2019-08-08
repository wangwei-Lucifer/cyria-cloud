package com.kunteng.cyria.dashboard.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import net.sf.json.JSONArray;
import com.alibaba.fastjson.JSONArray;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.time.LocalDate;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Document(collection = "publishes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Published {
	@Id
	private String id;

	private String hash;

	private Config config;

	private JSONArray widget;

	private Publish publish;

	private Boolean isTemplate;

	private Integer level;

	private String imgUrl;

	private String user;

	private long timestamp;

	public Published() {
		hash = Publish.generateId();
		config = new Config();
		widget = new JSONArray();
		publish = new Publish();
		isTemplate = false;
		level = 0;
		imgUrl = "";
		user = "";
		timestamp = new Date().getTime();
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
}
