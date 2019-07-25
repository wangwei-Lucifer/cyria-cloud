package com.kunteng.cyria.dashboard.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Translation {

	private String name;

	private String about;

	private Boolean isTemplate;

	private Integer level;

	private String imgUrl;

	private String user;
	
	private String template;
	
	private String mode;
	
	private String project;
	
	private Map<String,Object> colors;
	
	public Translation() {
		name = "";
		about = "";
		isTemplate = false;
		level = 0;
		imgUrl = "";
		user = "";
		project = "ungrouped";
		template = "";
		colors = new HashMap<String,Object>() {{put("name","配色1");put("value","['#8378ea', '#96bfff', '#37a2da', '#32c5e9', '#67e0e3', '#9fe6b8', '#ffdb5c', '#ff9f7f', '#fb7293', '#e062ae', '#e690d1', '#e7bcf3', '#9d96f4']");}};
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getAbout(){
		return this.about;
	}

	public void setAbout(String about){
		this.about = about;
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

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Map<String,Object> getColors() {
		return this.colors;
	}

	public void setColors(Map<String,Object> colors) {
		this.colors = colors;
	}

}
