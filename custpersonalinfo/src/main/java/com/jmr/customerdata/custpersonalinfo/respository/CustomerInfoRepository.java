package com.jmr.customerdata.custpersonalinfo.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmr.customerdata.custpersonalinfo.entity.CustomerInfo;

public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, String> {

	// CustomerInfo findById(String id); not required as this is available by
	// default
	// CustomerInfo findByCustomerNo(String id);
	// public CustomerInfo findByMobileNo(String mobileNo);

	// @Query(value = "Select * from STTM_CUST_PERSONAL where mobile_number=?1",
	// nativeQuery = true)
	// public Optional<CustomerInfo> findByCustomerNoAndMobileNo(String
	// customerNo, String mobileNo);

}