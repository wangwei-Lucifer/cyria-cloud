package com.kunteng.cyria.dashboard.client;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kunteng.cyria.dashboard.domain.User;
import com.kunteng.cyria.dashboard.domain.JWT;

@FeignClient(value = "auth-service", fallback = AuthServiceHystrix.class)
public interface AuthServiceClient {

	@RequestMapping(value = "/uaa/oauth/token",method = RequestMethod.POST)
	JWT getToken(@RequestHeader(value = "Authorization") String authorization, @RequestParam("grant_type") String type, 
			    @RequestParam("username") String username, @RequestParam("password") String password);
	
	@RequestMapping(value = "/uaa/users", method = RequestMethod.POST)
	void createUser(@Valid @RequestBody User user);
	
	@RequestMapping(value = "/uaa/users/login", method = RequestMethod.POST)
	String login(@RequestBody User user);
	
	@RequestMapping(value = "/uaa/users/register", method = RequestMethod.POST)
	String register(@RequestBody User user);
	
	@RequestMapping(value = "/uaa/users/getJWTState", method = RequestMethod.GET)
	String getJWTState(String jwtToken);
	
	@RequestMapping(value = "/uaa/users/refreshToken", method = RequestMethod.POST)
	String refreshToken(@RequestBody String token);
}
