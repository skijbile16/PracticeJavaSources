package com.jmr.customerdata.custpersonalinfo.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jmr.customerdata.custpersonalinfo.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, String>{
	
	@Query(value = "SELECT CASE WHEN EXISTS ( SELECT 1 FROM SMTB_USER WHERE USER_ID = :userId and HOME_BRANCH = :userBranch ) THEN 'true' ELSE 'false' END FROM DUAL", nativeQuery = true)
	boolean validateUserInfo(@Param("userId") String userId, @Param("userBranch") String userBranch);

	// @Query("select case when count(c)> 0 then true else false end from
	// COTM_SOURCE_PREF c where upper(c.source_code) =?1")
	// boolean validateSourceInfo(String source);


}
