package com.kunteng.cyria.dashboard.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kunteng.cyria.dashboard.client.AuthServiceClient;
import com.kunteng.cyria.dashboard.utils.CommonResult;

@Component
public class HTTPBearerAuthorizeAttribute implements  Filter {
	@Autowired
	private AuthServiceClient client;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
		System.out.println("filter init!");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		CommonResult resultMsg;
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=utf-8");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PATCH,PUT");
		httpResponse.setHeader("Access-Control-Max-Age", "3600");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,x-requested-with,X-Custom-Header,Content-Type,Accept,Authorization");
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String method = httpRequest.getMethod();
		if("OPTIONS".equalsIgnoreCase(method)) {
			httpResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
		}
		String auth = httpRequest.getHeader("Authorization");
		System.out.println("auth===="+auth);
		if(auth != null && auth.length() > 7) {
			String HeadStr = auth.substring(0,6).toLowerCase();
			if(HeadStr.compareTo("bbbbbb") == 0) {
				auth = auth.substring(7,auth.length());
				System.out.println("token===="+ auth);
				String tokenState = client.getJWTState(auth);
				System.out.println("tokenState===="+ tokenState);
				if(tokenState.equals("intime")) {
					chain.doFilter(request, response);
			 		return;
				}else if(tokenState.equals("refresh")) {
					String token = client.refreshToken(auth);
					if(token != null) {
						httpResponse.setHeader("Authorization", token);
						return;
					}
				}
			}
		}
		
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		ObjectMapper mapper = new ObjectMapper();
		resultMsg = new CommonResult().unauthorized("认证超时");
		httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));
		return;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
