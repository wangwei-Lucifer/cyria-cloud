package com.kunteng.cyria.dashboard.service;

import com.kunteng.cyria.dashboard.domain.Account;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.kunteng.cyria.dashboard.domain.Translation;

import java.util.List;

public interface AccountService {

	CommonResult getAccountByUsername(String username);
	CommonResult createNewAccount(Account account);
	CommonResult accountLogin(Account account);
	CommonResult accountLogout(String username);
}
