package com.jmr.customerdata.custpersonalinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmr.customerdata.custpersonalinfo.entity.CustomerInfo;
import com.jmr.customerdata.custpersonalinfo.exception.ResourceNotFoundException;
import com.jmr.customerdata.custpersonalinfo.respository.CustomerInfoRepository;

@Service
public class CustomerInfoService {

	@Autowired
	private CustomerInfoRepository customerInfoRepository;

	public List<CustomerInfo> getCustomer() {
		return customerInfoRepository.findAll();
	}

	public CustomerInfo getCustomerInfo(String id) {
		CustomerInfo opt = customerInfoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Customer Details Not Found for the Mobile Number: " + id));
		;
		// custInfoRepository.findByMobileNo(id);
		// if (opt.isPresent()) {
		// return opt.get();
		// }
		// opt.orElseThrow(Exception::new);
		return opt;
	}

	// public CustomerInfo getCustomerInfoCustom(CustInfoRequestDto
	// custInfoRequestDto) {
	// Optional<CustomerInfo> opt =
	// custInfoRepository.findByCustomerNoAndMobileNo(custInfoRequestDto.getCustomerno(),
	// custInfoRequestDto.getMobileno());
	// custInfoRepository.findByMobileNo(id);
	// if (opt.isPresent()) {
	// return opt.get();
	// }
	// opt.orElseThrow(Exception::new);
	// return null;
	// }

}