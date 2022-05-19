package com.jmr.customerdata.custpersonalinfo.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jmr.customerdata.custpersonalinfo.entity.AccountListInfo;

public interface AccountListInfoRepository extends JpaRepository<AccountListInfo, String> {

	/*
	 * @Query(value =
	 * "SELECT B.CUST_AC_NO, B.AC_DESC FROM STTM_CUST_PERSONAL A, STTM_CUST_ACCOUNT B  WHERE A.CUSTOMER_NO = B.CUST_NO AND A.CUSTOMER_NO=(SELECT CUSTOMER_NO FROM STTM_CUST_PERSONAL WHERE MOBILE_NUMBER =:mobileno)"
	 * , nativeQuery = true) List<AccountListInfo>
	 * getAccountListByMobileNumber(@Param("mobileno") String mobileno);
	 */

	@Query(value = "SELECT CUST_AC_NO, AC_DESC, ACCOUNT_CLASS, CCY, ACC_STATUS FROM STTM_CUST_ACCOUNT  WHERE  CUST_NO= :customerno", nativeQuery = true)
	List<AccountListInfo> getAccountListByCustNo(@Param("customerno") String customerno);

}
