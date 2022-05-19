package com.jmr.customerdata.custpersonalinfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STTM_CUST_PERSONAL")
// @IdClass(CustomerInfo.class)
public class CustomerInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MOBILE_NUMBER")
	private String mobileNo;

	@Column(name = "CUSTOMER_NO")
	private String customerNo;

	@Column(name = "FIRST_NAME")
	private String custFirstName;

	@Column(name = "MIDDLE_NAME")
	private String custMiddleName;

	@Column(name = "LAST_NAME")
	private String custLastName;

	@Column(name = "P_NATIONAL_ID")
	private String nationalId;

}