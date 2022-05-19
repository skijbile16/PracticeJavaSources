package com.jmr.customerdata.custpersonalinfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STTM_CUST_ACCOUNT")
public class AccountListInfo {
	
	/*
	 * @Column(name = "CUST_NO") private String customerNo;
	 */
	
	@Id
	@Column(name = "CUST_AC_NO")
	private String accountNo;
	
	/*
	 * @Column(name = "BRANCH_CODE") private String branchCode;
	 */
	
	@Column(name = "AC_DESC")
	private String accountDesc;
	
	@Column(name = "ACCOUNT_CLASS")
	private String accountType;
	
	@Column(name = "CCY")
	private String accountCurrency;

	@Column(name = "ACC_STATUS")
	private String accountStatus;

}
