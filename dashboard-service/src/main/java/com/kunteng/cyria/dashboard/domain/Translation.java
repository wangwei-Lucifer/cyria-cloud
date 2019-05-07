package com.kunteng.cyria.dashboard.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

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
	
	private String templateId;
	
	public Translation() {
		name = "";
		about = "";
		isTemplate = false;
		level = 0;
		imgUrl = "";
		user = "";
		templateId = "";
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

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

}
