package com.kunteng.cyria.dashboard.service;

import com.kunteng.cyria.dashboard.client.AuthServiceClient;
import com.kunteng.cyria.dashboard.domain.Account;
import com.kunteng.cyria.dashboard.domain.Dashboard;
import com.kunteng.cyria.dashboard.domain.JWT;
import com.kunteng.cyria.dashboard.domain.User;
import com.kunteng.cyria.dashboard.domain.UserLoginDTO;
import com.kunteng.cyria.dashboard.repository.AccountRepository;
import com.kunteng.cyria.dashboard.repository.DashboardRepository;
import com.kunteng.cyria.dashboard.utils.BPwdEncoderUtil;
import com.kunteng.cyria.dashboard.utils.CommonResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.time.LocalDate;

@Service
public class AccountServiceImpl implements AccountService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AuthServiceClient client;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private DashboardRepository dashboardRepository;

	public CommonResult getAccountByUsername(String id){
		Account account = accountRepository.findAccountById(id);
		return new CommonResult().success(account);
	}

	public CommonResult createNewAccount(Account account) {
		Account exist = accountRepository.findAccountByUsername(account.getUsername());
		if(exist != null) {
			return new CommonResult().customFailed("用户已存在");
		}
		String password = account.getPassword();
		System.out.println("account.password="+password);
		account.setPassword(BPwdEncoderUtil.BCryptPassword(password));
		Account result = accountRepository.save(account);
		
		User user = new User();
		user.setUsername(account.getUsername());
		user.setPassword(password);
		//client.createUser(user);
		client.register(user);
		return new CommonResult().success(result);
	}

	public CommonResult accountLogin(Account account) {
		CommonResult result = new CommonResult();
		User user = new User();
		
		Account accountr = accountRepository.findAccountByUsername(account.getUsername());
		if(null == accountr) {
			result.setCode(1);
			result.setMsg("用户不存在");
			return result;
		}
		if(!BPwdEncoderUtil.matches(account.getPassword(), accountr.getPassword())){
			System.out.println("password="+account.getPassword());
			result.setCode(1);
			result.setMsg("密码错误");
			return result;
		}
		
		user.setUsername(account.getUsername());
		user.setPassword(account.getPassword());
		
	//	JWT jwt = client.getToken("Basic ZGFzaGJvYXJkLXNlcnZpY2U6YWRtaW4=","password", account.getUsername(), account.getPassword());
		String jwt = client.login(user);
		if(jwt == null) {
			result.setCode(1);
			result.setMsg("获取token失败");
			return result;
		}

		
	//	UserLoginDTO userLoginDTO = new UserLoginDTO();
	//	userLoginDTO.setJwt(jwt);
	//	userLoginDTO.setUser(user);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = servletRequestAttributes.getResponse();
    	response.addHeader("Authorization", jwt);
	//	response.addHeader("Authorization","testToken");
		
		result.setCode(0);
		result.setId(accountr.getId());
	//	result.setData(userLoginDTO);
		result.setMsg("登录成功");
	//	result.setData(jwt);
		
		return result;
		
	}

	public CommonResult accountLogout(String username){
		Account account = accountRepository.findAccountByUsername(username);
		return new CommonResult().success(account);
	}
	
	public CommonResult updateProject(String id, Map<String, Map<String,String>> map) {
		Account account = accountRepository.findAccountById(id);
		if(!account.getId().equals("")) {
			account.setProjects(map.get("projects"));
			accountRepository.save(account);
			return new CommonResult().success("更新分组成功");
		}
		return new CommonResult().customFailed("更新用户分组失败");
	}
	
	public CommonResult deleteProject(String id, String key, Map<String,Map<String,String>> map) {
		System.out.println("id="+id);
		System.out.println("key="+key);
		System.out.println("map.size="+map.size());
		Account account = accountRepository.findAccountById(id);
		if(!account.getId().equals("")) {
			account.setProjects(map.get("projects"));
			accountRepository.save(account);
			List<Dashboard> dashboard = dashboardRepository.findByProject(key);
			for (int i =0 ;i < dashboard.size(); i++) {
				Dashboard db = dashboard.get(i);
				db.setProject("ungrouped");
				dashboardRepository.save(db);
			}
			return new CommonResult().success("更新用户分组成功");
		}
		return new CommonResult().customFailed("更新用户分组成功");
	}
}
