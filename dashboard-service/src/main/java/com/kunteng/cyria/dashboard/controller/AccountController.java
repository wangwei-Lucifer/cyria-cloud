package com.kunteng.cyria.dashboard.controller;

import com.kunteng.cyria.dashboard.domain.Account;
import com.kunteng.cyria.dashboard.service.AccountService;
import com.kunteng.cyria.dashboard.utils.CommonResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(path = "/user/{id}/info", method = RequestMethod.GET)
	public CommonResult getUserByUsername(@PathVariable String id ){
		System.out.println("getUserByUsername");
		return accountService.getAccountByUsername(id);
	}

	@RequestMapping(path = "/user/register", method = RequestMethod.POST)
	public CommonResult createNewUser(@Valid @RequestBody Account account) {
		return accountService.createNewAccount(account);
	}

	@RequestMapping(path = "/user/login", method = RequestMethod.POST)
	public CommonResult accountLogin(@RequestBody Account account) {
		return accountService.accountLogin(account);
	}
	
	@RequestMapping(path = "/user/logout", method = RequestMethod.POST)
	public CommonResult accountLogout(@RequestBody String username){
		return accountService.accountLogout(username);
	}
	
	@RequestMapping(path = "/user/{id}/projects", method = RequestMethod.POST)
	public CommonResult updateProject(@PathVariable String id, @RequestBody Map<String,Map<String,String>> map) {
		return accountService.updateProject(id, map);
	}
	
	@RequestMapping(path = "/user/{id}/projects/{key}", method = RequestMethod.DELETE)
	public CommonResult deleteProject(@PathVariable String id, @PathVariable String key, @RequestParam Map<String,Map<String,String>> map) {
		return accountService.deleteProject(id, key, map);
	}
}
