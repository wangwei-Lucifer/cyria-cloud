package com.kunteng.cyria.dashboard.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.time.LocalDate;

@Document(collection = "accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

	@Id
	private String id;

	@NotNull
	@Length(min = 3, max = 20)
	private String username;

	@NotNull
	@Length(min = 6, max = 40)
	private String password;

	private String email;

	private String phone;

	private LocalDate createTime;

	private List<String> roles;

	private String group;

	private String avatar;

	private String city;

	public Account(){
	
	}

	public Account(String username, String password, List<String> roles){
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public String getId() {
		return this.id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail(){
		return this.email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public LocalDate getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(LocalDate createTime){
		this.createTime = createTime;
	}

	public List<String> getRoles(){
		return this.roles;
	}

	public void setRoles(List<String> roles){
		this.roles = roles;
	}

	public String getGroup(){
		return group;
	}

	public void setGroup(String group){
		this.group = group;
	}

	public String getAvatar(){
		return this.avatar;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getCity(){
		return this.city;
	}

	public void setCity(String city){
		this.city = city;
	}
}
