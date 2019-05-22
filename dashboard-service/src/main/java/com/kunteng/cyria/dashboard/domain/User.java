package com.kunteng.cyria.dashboard.domain;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.List;

public class User{

	@Id
	private String id;
	
	private String username;

	private String password;
	
    private String isAdmin;
	
	private String jwtToken;
	
	private String lastActTime;

	private List<Role> authorities;
	
	public User() {
		
	}

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIsAdmin() {
		return this.isAdmin;
	}
	
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public String getJwtToken() {
		return this.jwtToken;
	}
	
	public void SetJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	public String getLastActTime() {
		return this.lastActTime;
	}
	
	public void setLastActTime(String lastActTime) {
		this.lastActTime = lastActTime;
	}
	
	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return this.authorities;
	}

	public void setAuthorities(List<Role> authorities){
		this.authorities = authorities;
	}
}
