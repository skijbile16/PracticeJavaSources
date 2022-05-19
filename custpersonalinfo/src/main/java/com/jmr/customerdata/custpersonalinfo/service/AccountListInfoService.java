package com.jmr.customerdata.custpersonalinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmr.customerdata.custpersonalinfo.entity.AccountListInfo;
import com.jmr.customerdata.custpersonalinfo.respository.AccountListInfoRepository;

@Service
public class AccountListInfoService {

	@Autowired
	private AccountListInfoRepository accountListInfoRepository;

	/*
	public List<AccountListInfo> getAccountListByMobileNumber(@Param("mobileno") String mobileno) {
		return accountListInfoRepository.getAccountListByMobileNumber(mobileno);
	}
	*/

	public List<AccountListInfo> getAccountListById(String customerNo) {
		return accountListInfoRepository.getAccountListByCustNo(customerNo);
	}
}
