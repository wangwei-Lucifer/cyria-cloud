package com.kunteng.cyria.dashboard.service;

import com.kunteng.cyria.dashboard.domain.Account;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.kunteng.cyria.dashboard.domain.Translation;

import java.util.List;
import java.util.Map;

public interface AccountService {

	CommonResult getAccountByUsername(String username);
	CommonResult createNewAccount(Account account);
	CommonResult accountLogin(Account account);
	CommonResult accountLogout(String username);
	CommonResult updateProject(String id, Map<String, Map<String,String>> map);
	CommonResult deleteProject(String id, String key, Map<String, Map<String,String>> map);
}
