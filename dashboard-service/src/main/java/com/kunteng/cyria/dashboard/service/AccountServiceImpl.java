package com.kunteng.cyria.dashboard.service;

import com.kunteng.cyria.dashboard.domain.Account;
import com.kunteng.cyria.dashboard.repository.AccountRepository;
import com.kunteng.cyria.dashboard.utils.CommonResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

@Service
public class AccountServiceImpl implements AccountService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountRepository accountRepository;

	public CommonResult getAccountByUsername(String username){
		Account account = accountRepository.findAccountByUsername(username);
		return new CommonResult().success(account);
	}

	public CommonResult createNewAccount(Account account) {
		Account result = accountRepository.save(account);
		return new CommonResult().success(result);
	}

	public CommonResult accountLogin(Account account) {
		Account accountr = accountRepository.findAccountByUsername(account.getUsername());
		if(accountr.getPassword().equals(account.getPassword())){
			return new CommonResult().success(accountr.getUsername());
		}else{
			return new CommonResult().failed();
		}
	}

	public CommonResult accountLogout(String username){
		Account account = accountRepository.findAccountByUsername(username);
		return new CommonResult().success(account);
	}
}
