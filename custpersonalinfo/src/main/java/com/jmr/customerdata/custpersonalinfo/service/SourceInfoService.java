package com.jmr.customerdata.custpersonalinfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmr.customerdata.custpersonalinfo.respository.SourceInfoRepository;

@Service
public class SourceInfoService {

	@Autowired
	private SourceInfoRepository sourceInfoRepository;

	public boolean validateSourceInfo(String source) {

		System.out.println("Inside validateSourceInfo Method");
		System.out.println("Value of the Input Parameter: " + source);

		return sourceInfoRepository.validateSourceInfo(source);
		// return sourceInfoRepository.existsById(source);
	}

}
