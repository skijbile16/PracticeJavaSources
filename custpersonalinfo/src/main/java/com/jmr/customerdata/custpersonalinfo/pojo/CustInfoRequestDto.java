package com.jmr.customerdata.custpersonalinfo.pojo;

import javax.validation.constraints.NotNull;

public class CustInfoRequestDto {

	@NotNull(message = "Mobile number cannot be missing or empty")
	//@Min(10)
	private String mobileno;

	public CustInfoRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustInfoRequestDto(String mobileno) {
		super();
		this.mobileno = mobileno;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

}
