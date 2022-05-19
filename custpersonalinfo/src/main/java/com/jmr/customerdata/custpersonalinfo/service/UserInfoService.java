package com.jmr.customerdata.custpersonalinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmr.customerdata.custpersonalinfo.respository.UserInfoRepository;

@Service
public class UserInfoService {

	@Autowired
	private UserInfoRepository userInfoRepository;

	public boolean validateSourceInfo(String userId, String userBranch) {

		System.out.println("Inside validateSourceInfo Method");
		System.out.println("Value of the Input Parameters UserId : " + userId + " UserBranch : " + userBranch);

		return userInfoRepository.validateUserInfo(userId, userBranch);
		// return sourceInfoRepository.existsById(source);
	}

}
