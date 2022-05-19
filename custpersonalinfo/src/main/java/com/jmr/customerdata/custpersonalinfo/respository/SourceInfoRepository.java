package com.jmr.customerdata.custpersonalinfo.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jmr.customerdata.custpersonalinfo.entity.SourceInfo;

public interface SourceInfoRepository extends JpaRepository<SourceInfo, String> {

	@Query(value = "SELECT CASE WHEN EXISTS ( SELECT 1 FROM COTM_SOURCE_PREF WHERE SOURCE_CODE = :source ) THEN 'true' ELSE 'false' END FROM DUAL", nativeQuery = true)
	boolean validateSourceInfo(@Param("source") String source);

	// @Query("select case when count(c)> 0 then true else false end from
	// COTM_SOURCE_PREF c where upper(c.source_code) =?1")
	// boolean validateSourceInfo(String source);

}
