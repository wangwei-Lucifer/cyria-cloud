package com.kunteng.cyria.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
public class User implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
//	@Column(nullable=false, unique=true)
	private String username;

//	@Column()
	private String password;
	
	private String isAdmin;
	
	private String jwtToken;
	
	private String lastActTime;

//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id",referencedColumnName="id",inverseJoinColumns=@JoinColumn(name="role_id",referencedColumnName="id")))
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
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	public void setAuthorities(List<Role> authorities) {
		this.authorities = authorities;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	//@Override
	//public List<GrantedAuthority> getAuthorities() {
	//	return null;
	//}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
