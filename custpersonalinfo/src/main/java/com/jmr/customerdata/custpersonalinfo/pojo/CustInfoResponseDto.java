package com.jmr.customerdata.custpersonalinfo.pojo;

import com.jmr.customerdata.custpersonalinfo.entity.CustomerInfo;

public class CustInfoResponseDto {

	private int returnCode;
	private String returnMsg;
	private boolean errorMsg;
	private CustomerInfo returnData;

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public boolean isErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(boolean errorMsg) {
		this.errorMsg = errorMsg;
	}

	public CustomerInfo getReturnData() {
		return returnData;
	}

	public void setReturnData(CustomerInfo returnData) {
		this.returnData = returnData;
	}

}
