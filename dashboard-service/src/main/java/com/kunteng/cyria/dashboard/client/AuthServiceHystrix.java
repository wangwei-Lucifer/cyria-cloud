package com.kunteng.cyria.dashboard.client;

import javax.validation.Valid;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kunteng.cyria.dashboard.domain.JWT;
import com.kunteng.cyria.dashboard.domain.User;

@Component
public class AuthServiceHystrix implements AuthServiceClient {
	@Override
	public JWT getToken(String authorization, String type, String username, String password) {
		System.out.println("fallback:getToken");
		return null;
	}
	
	@Override
	public void createUser(@Valid @RequestBody User user) {
		System.out.println("fallback="+user.getUsername());
	}
	
	@Override
	public String login(@RequestBody User user) {
		return null;
		
	}
	
	public String register(@RequestBody User user) {
		return null;
		
	}
	
	public String getJWTState(String jwtToken) {
		return null;
	}
	

	public String refreshToken(@RequestBody String token) {
		return null;
	}
}
