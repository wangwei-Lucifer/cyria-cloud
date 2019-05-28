package com.kunteng.cyria.dashboard.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

	private Date createTime;

	private List<Role> authorities;
	
	private ArrayList<String> roles;

	private String group;

	private String avatar;

	private String city;

	public Account(){
	
	}

	public Account(String username, String password, List<Role> authorities){
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.roles  = new ArrayList<String>() {{add("user");add("admin");}}; 
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

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Collection<? extends GrantedAuthority> getAuthorities(){
		return this.authorities;
	}

	public void setAuthorities(List<Role> authorities){
		this.authorities = authorities;
	}
	
	public ArrayList<String> getRoles(){
		return this.roles;
	}
	
	public void setRoles(ArrayList<String> roles) {
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
